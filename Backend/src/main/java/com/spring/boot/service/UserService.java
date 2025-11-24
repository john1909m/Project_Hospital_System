package com.spring.boot.service;

import com.spring.boot.dto.UserDto;
import com.spring.boot.model.User;
import jakarta.transaction.SystemException;

import java.util.List;

public interface UserService {

    UserDto addUser(UserDto userDto) throws SystemException;
    UserDto adminAddUser(UserDto userDto) throws SystemException;
    UserDto getUserByUsername(String username);
    List<UserDto> getAllUsers();
    void deleteUser(Long id);

}
