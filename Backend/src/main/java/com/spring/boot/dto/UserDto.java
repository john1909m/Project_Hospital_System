package com.spring.boot.dto;

import com.spring.boot.model.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long userId;

    private String username;

    private String email;

    private String password;

    private Set<Role> roles;
    private String roleType;

    private DoctorDto doctor;

    private PatientDto patient;
}
