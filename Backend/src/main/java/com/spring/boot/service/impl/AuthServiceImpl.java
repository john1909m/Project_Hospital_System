package com.spring.boot.service.impl;

import com.spring.boot.config.jwt.TokenHandler;
import com.spring.boot.controller.vm.LoginRequestVM;
import com.spring.boot.controller.vm.LoginResponseVM;
import com.spring.boot.dto.UserDto;
import com.spring.boot.mapper.UserMapper;
import com.spring.boot.model.Role;
import com.spring.boot.model.User;
import com.spring.boot.repo.UserRepo;
import com.spring.boot.service.AuthService;
import com.spring.boot.service.UserService;
import jakarta.transaction.SystemException;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private TokenHandler tokenHandler;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public void signUp(UserDto userDto) throws SystemException {
        userService.addUser(userDto);
    }

    @Override
    public LoginResponseVM login(LoginRequestVM loginRequestVm) throws SystemException {
//        User user = userRepo.findByUsername(loginRequestVm.getUsername())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//        if (!passwordEncoder.matches(loginRequestVm.getPassword(), user.getPassword())) {
//            throw new SystemException("invalid.password.error");
//        }
//        Authentication authentication=authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(loginRequestVm.getUsername(),loginRequestVm.getPassword())
//        );
//        UserDto userDto=userService.getUserByUsername(loginRequestVm.getUsername());
//        user=userRepo.findByUsername(loginRequestVm.getUsername()).orElseThrow();
//        Set<String> roles = user.getRoles()
//                .stream()
//                .map(Role::getRoleName)
//                .collect(Collectors.toSet());
//        userDto.setRoles(user.getRoles().stream().map(role -> role).collect(Collectors.toSet()));
//        userDto.setPassword(null);
//        String token = tokenHandler.createToken(userDto);
//        return new LoginResponseVM(token,userDto);



        // جلب user مباشرة من repo
        User user = userRepo.findByUsername(loginRequestVm.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // تحقق من الباسورد
        if (!passwordEncoder.matches(loginRequestVm.getPassword(), user.getPassword())) {
            throw new SystemException("invalid.password.error");
        }

        // هنا ممكن تعمل authentication يدوي بدون استدعاء authenticationManager
        UserDto userDto = userMapper.toDto(user);
        userDto.setPassword(null);

        // إنشاء token
        String token = tokenHandler.createToken(userDto);

        return new LoginResponseVM(token, userDto);
    }
}
