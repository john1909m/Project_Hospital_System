package com.spring.boot.service;

import com.spring.boot.dto.RoleDto;
import jakarta.transaction.SystemException;

public interface RoleService {
    RoleDto findByRoleName(String roleName) throws SystemException;
}
