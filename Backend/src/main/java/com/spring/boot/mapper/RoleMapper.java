package com.spring.boot.mapper;

import com.spring.boot.dto.RoleDto;
import com.spring.boot.dto.UserDto;
import com.spring.boot.model.Role;
import com.spring.boot.model.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    Role toEntity(RoleDto roleDto);
    RoleDto toDto(Role role);
    List<RoleDto> toDto(List<Role> roles);
    List<Role> toEntity(List<RoleDto> roleDtos);
}
