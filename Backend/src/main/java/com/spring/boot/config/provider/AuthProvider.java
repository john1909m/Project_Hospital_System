package com.spring.boot.config.provider;

import com.spring.boot.dto.UserDto;
import com.spring.boot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

//@Component
@Deprecated
public class AuthProvider implements AuthenticationProvider {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userName = authentication.getPrincipal().toString();
        String password = authentication.getCredentials().toString(); // not

        UserDto userDto = userService.getUserByUsername(userName);

        if (!passwordEncoder.matches(password, userDto.getPassword())) {
            throw new BadCredentialsException("invalid password");
        }


        List<SimpleGrantedAuthority> roles =  userDto.getRoles().stream()
                .map(roleDto -> new SimpleGrantedAuthority("ROLE_" + roleDto.getRoleName())).collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(userDto.getUsername(), password, roles);

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}

