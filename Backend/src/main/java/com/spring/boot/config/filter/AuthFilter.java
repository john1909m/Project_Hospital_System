package com.spring.boot.config.filter;

import com.spring.boot.config.jwt.TokenHandler;
import com.spring.boot.repo.UserRepo;
import com.spring.boot.dto.UserDto;
import com.spring.boot.model.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuthFilter extends OncePerRequestFilter {


    private final TokenHandler tokenHandler;


    public AuthFilter(TokenHandler tokenHandler) {
        this.tokenHandler = tokenHandler;

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            System.out.println("Token received: " + token); // DEBUG
            UserDto userDto = tokenHandler.validateToken(token);
            if (userDto != null) {
                System.out.println("Token valid, user: " + userDto.getUsername());
                List<SimpleGrantedAuthority> roles = userDto.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleName().toUpperCase().trim()))
                        .collect(Collectors.toList());
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(userDto, null, roles);
                System.out.println("Roles in token: " + roles);
                SecurityContextHolder.getContext().setAuthentication(auth);
                System.out.println("Auth set: " + auth.getPrincipal() + " | Roles: " + roles);

            }else {
                System.out.println("Token invalid or expired");
            }
        }
        System.out.println("Request URI: " + request.getRequestURI());
        System.out.println("Authorization header: " + token);
        filterChain.doFilter(request, response);
    }



    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        String uri = request.getRequestURI();
//        System.out.println("URI: " + request.getRequestURI());
//        System.out.println("Auth in context: " + SecurityContextHolder.getContext().getAuthentication());
//        System.out.println("Method: " + request.getMethod());
//        System.out.println("RequestURI Raw: " + request.getRequestURL());
        System.out.println("shouldNotFilter: " + request.getRequestURI());
        System.out.println("Passed security config: " + request.getRequestURI());

        return uri.startsWith("/auth/");
    }
}
