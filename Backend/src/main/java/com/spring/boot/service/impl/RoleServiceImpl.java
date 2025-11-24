package com.spring.boot.service.impl;

import com.spring.boot.dto.RoleDto;
import com.spring.boot.mapper.RoleMapper;
import com.spring.boot.model.Role;
import com.spring.boot.repo.RoleRepo;
import com.spring.boot.service.RoleService;
import jakarta.transaction.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class RoleServiceImpl implements RoleService {

    private RoleRepo roleRepo;

    private RoleMapper roleMapper;

    @Autowired
    public RoleServiceImpl(RoleRepo roleRepo, RoleMapper roleMapper) {
        this.roleRepo = roleRepo;
        this.roleMapper = roleMapper;
    }

    @Override
    public RoleDto findByRoleName(String roleName) throws SystemException {
        Optional<Role> role = roleRepo.findByRoleName(roleName);

        if (!role.isPresent()) {
            throw new SystemException("role.not.exist");
        }

        return roleMapper.toDto(role.get());
    }
}
