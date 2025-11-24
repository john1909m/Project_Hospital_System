package com.spring.boot.service;

import com.spring.boot.controller.vm.LoginRequestVM;
import com.spring.boot.controller.vm.LoginResponseVM;
import com.spring.boot.dto.UserDto;
import jakarta.transaction.SystemException;

public interface AuthService {
    void signUp(UserDto userDto) throws SystemException;
    LoginResponseVM login(LoginRequestVM loginRequestVm) throws SystemException;
}
