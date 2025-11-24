package com.spring.boot.config.jwt;

import com.spring.boot.dto.UserDto;
import com.spring.boot.helper.JwtToken;
import com.spring.boot.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class TokenHandler {
    private String secret;

    private Duration time;

    private JwtBuilder jwtBuilder;

    private JwtParser jwtParser;

    private UserService userService;


    @Autowired
    public TokenHandler( JwtToken jwtToken,UserService userService){
        this.userService = userService;

        this.secret = jwtToken.getSecret();
        this.time = jwtToken.getTime();

        Key key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        jwtBuilder = Jwts.builder().signWith(key);
        jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
    }

    public String createToken(UserDto userDto){
        Date issueData = new Date(); // 1/11/2025
        Date expiryData = Date.from(issueData.toInstant().plus(time)); // 1/12/2025

        jwtBuilder.setSubject(userDto.getUsername());
        jwtBuilder.setIssuedAt(issueData);
        jwtBuilder.setExpiration(expiryData);
        List<String> roles = userDto.getRoles().stream().map(roleDto -> roleDto.getRoleName()).collect(Collectors.toList());
        jwtBuilder.claim("roles", roles);

        return jwtBuilder.compact();
    }

    public UserDto validateToken(String token){
        try {
            if (!jwtParser.isSigned(token)) {
                return null;
            }

            Claims claims = jwtParser.parseClaimsJws(token).getBody();
            Date issueData = claims.getIssuedAt();
            Date expiryData = claims.getExpiration();
            String userName = claims.getSubject();

            UserDto userDto = userService.getUserByUsername(userName);


            boolean isActiveToken = expiryData.after(issueData) &&  expiryData.after(new Date());

            if (isActiveToken && Objects.nonNull(userDto)) {
                return userDto;
            }

            return null;
        } catch (Exception exception) {
            return null;
        }
    }
}
