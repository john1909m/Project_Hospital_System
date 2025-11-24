package com.spring.boot.controller;

import com.spring.boot.dto.DoctorDto;
import com.spring.boot.dto.PatientDto;
import com.spring.boot.dto.UserDto;
import com.spring.boot.model.Appointment;
import com.spring.boot.model.Doctor;
import com.spring.boot.model.Patient;
import com.spring.boot.service.DoctorService;
import com.spring.boot.service.UserService;
import jakarta.transaction.SystemException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5174/")
@RestController
public class DoctorController {

    private final UserService userService;
    public DoctorService doctorService;

    @Autowired
    public DoctorController(DoctorService doctorService, UserService userService) {
        this.doctorService = doctorService;
        this.userService = userService;
    }

    @GetMapping("/doctors")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR' , 'PATIENT')")
    public ResponseEntity<List<DoctorDto>> getDoctors() {
        return ResponseEntity.ok().body(doctorService.getDoctors());
    }

    @PostMapping("/doctor/add")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<DoctorDto> addDoctor(@RequestBody @Valid DoctorDto doctorDto) throws URISyntaxException {
        return ResponseEntity.created(new URI("/doctor/add")).body(doctorService.addDoctor(doctorDto));
    }

    @PutMapping("/doctor/update")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<DoctorDto> updateDoctor(@RequestBody  DoctorDto doctorDto) {
        return ResponseEntity.ok().body(doctorService.updateDoctor(doctorDto));
    }

    @DeleteMapping("/doctor/delete/{doctorId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long doctorId){
        doctorService.deleteDoctor(doctorId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/doctor/id/{doctorId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR' , 'PATIENT')")
    public ResponseEntity<DoctorDto> getDoctorById(@PathVariable Long doctorId){
        return ResponseEntity.ok().body(doctorService.getDoctorById(doctorId));
    }

    @GetMapping("/doctor/name/{doctorName}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR' , 'PATIENT')")
    public ResponseEntity<DoctorDto> getDoctorByName(@PathVariable String doctorName){
        return ResponseEntity.ok().body(doctorService.getDoctorByName(doctorName));
    }

    @GetMapping("/{doctorId}/appointments")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<List<Appointment>> getDoctorAppointments(@PathVariable Long doctorId) {
        return ResponseEntity.ok().body(doctorService.getAppointmentsForDoctor(doctorId));
    }

    @GetMapping("/{doctorId}/patients")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<List<PatientDto>> getDoctorPatients(@PathVariable Long doctorId) {
        return ResponseEntity.ok().body(doctorService.getPatientsForDoctor(doctorId));
    }

    @PostMapping("/admin/add/user")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<UserDto> adminAddUser(@RequestBody @Valid UserDto userDto) throws SystemException {
        return ResponseEntity.ok().body(userService.adminAddUser(userDto));

    }








}
