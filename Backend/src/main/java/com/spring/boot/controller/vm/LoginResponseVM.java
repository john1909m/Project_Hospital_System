package com.spring.boot.controller.vm;

import com.spring.boot.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class LoginResponseVM {

    String token;
    private UserDto user;

}
