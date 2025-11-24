package com.spring.boot.service.impl;

import com.spring.boot.dto.PatientDto;
import com.spring.boot.dto.RoleDto;
import com.spring.boot.dto.UserDto;
import com.spring.boot.mapper.DoctorMapper;
import com.spring.boot.mapper.PatientMapper;
import com.spring.boot.mapper.RoleMapper;
import com.spring.boot.mapper.UserMapper;
import com.spring.boot.model.Doctor;
import com.spring.boot.model.Patient;
import com.spring.boot.model.Role;
import com.spring.boot.model.User;
import com.spring.boot.repo.RoleRepo;
import com.spring.boot.repo.UserRepo;
import com.spring.boot.service.DoctorService;
import com.spring.boot.service.PatientService;
import com.spring.boot.service.RoleService;
import com.spring.boot.service.UserService;
import jakarta.transaction.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    private final DoctorService doctorService;
    private final DoctorMapper doctorMapper;
    private UserRepo userRepo;
    private RoleRepo roleRepo;
    private RoleService roleService;
    private UserMapper userMapper;
    private PatientMapper patientMapper;
    private RoleMapper roleMapper;
    private PasswordEncoder passwordEncoder;
    private PatientService patientService;

    @Autowired
    public UserServiceImpl(UserRepo userRepo,
                           RoleService roleService,
                           RoleMapper roleMapper,
                           PasswordEncoder passwordEncoder,
                           RoleRepo roleRepo,
                           UserMapper userMapper,
                           PatientService patientService,
                           PatientMapper patientMapper, DoctorService doctorService, DoctorMapper doctorMapper) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.roleRepo = roleRepo;
        this.userMapper = userMapper;
        this.roleService = roleService;
        this.roleMapper = roleMapper;
        this.patientService = patientService;
        this.patientMapper = patientMapper;
        this.doctorService = doctorService;
        this.doctorMapper = doctorMapper;
    }

    @Override
    public UserDto addUser(UserDto userDto) throws SystemException {

        if (userDto.getUserId() != null) {
            throw new RuntimeException("id.user.not.required");
        }
        Optional<User> userOptional = userRepo.findByUsername(userDto.getUsername());
        if(userOptional.isPresent()){
            throw new RuntimeException("exist user with same userName: " + userDto.getUsername());
        }
        // تحويل DTO لـ Entity وحفظ الـ User
        User user = userMapper.toEntity(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        RoleDto roleDto = roleService.findByRoleName("PATIENT");
        user.setRoles(Arrays.asList(roleMapper.toEntity(roleDto)));

        User userSaved = userRepo.saveAndFlush(user); // حفظ الـ User أولًا
        UserDto response = userMapper.toDto(userSaved);


        if (userDto.getPatient() != null) {
            Patient patient = new Patient();
            patient.setPatientName(userDto.getPatient().getPatientName());
            patient.setPatientPhone(userDto.getPatient().getPatientPhone());
            patient.setPatientGender(userDto.getPatient().getPatientGender());
            patient.setPatientAge(userDto.getPatient().getPatientAge());
            patient.setUser(userSaved);

//            PatientDto savedPatient = patientService.addPatient(patientMapper.toDto(patient));

            Patient savedPatient = patientService.addPatientEntity(patient);
            response.setPatient(patientMapper.toDto(savedPatient));
        }

        return response;
    }

    @Override
    public UserDto adminAddUser(UserDto userDto) throws SystemException {
        if (userDto.getUserId() != null) {
            throw new RuntimeException("id.user.not.required");
        }

        Optional<User> userOptional = userRepo.findByUsername(userDto.getUsername());
        if(userOptional.isPresent()){
            throw new RuntimeException("exist user with same userName: " + userDto.getUsername());
        }
        User user = userMapper.toEntity(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        Set<Role> roles = userDto.getRoles();
        String roleName = roles.iterator().next().getRoleName();

        RoleDto roleDto = roleService.findByRoleName(roleName);
        user.setRoles(Arrays.asList(roleMapper.toEntity(roleDto)));

        User userSaved = userRepo.saveAndFlush(user); // حفظ الـ User أولًا
        UserDto response = userMapper.toDto(userSaved);

        if (userDto.getPatient() != null) {
            Patient patient = new Patient();
            patient.setPatientName(userDto.getPatient().getPatientName());
            patient.setPatientPhone(userDto.getPatient().getPatientPhone());
            patient.setPatientGender(userDto.getPatient().getPatientGender());
            patient.setPatientAge(userDto.getPatient().getPatientAge());
            patient.setUser(userSaved);
            Patient savedPatient = patientService.addPatientEntity(patient);
            response.setPatient(patientMapper.toDto(savedPatient));
        }
        if(userDto.getDoctor() != null) {
            Doctor doctor = new Doctor();
            doctor.setDoctorName(userDto.getDoctor().getDoctorName());
            doctor.setDoctorPhone(userDto.getDoctor().getDoctorPhone());
            doctor.setDoctorDepartment(userDto.getDoctor().getDoctorDepartment());
            doctor.setUser(userSaved);
            Doctor savedDoctor = doctorService.addDoctorEntity(doctor);
            response.setDoctor(doctorMapper.toDto(savedDoctor));
        }

        return response;
    }

    @Override
    public UserDto getUserByUsername(String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepo.findAll().stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepo.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepo.deleteById(id);
    }
}
