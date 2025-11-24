package com.spring.boot.controller;

import com.spring.boot.dto.AppointmentDto;
import com.spring.boot.model.Appointment;
import com.spring.boot.service.AppointmentService;
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
public class AppointmentController {
    private AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping("/appointments")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<List<AppointmentDto>> getAppointments() {
        return ResponseEntity.ok().body(appointmentService.getAppointments());
    }

    @GetMapping("/appointment/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<AppointmentDto> getAppointmentById(@PathVariable Long id) {
        return ResponseEntity.ok().body(appointmentService.getAppointmentById(id));
    }

    @GetMapping("/appointment/patient/{patientId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR','PATIENT')")
    public ResponseEntity<List<AppointmentDto>> getAppointmentPatientId(@PathVariable Long patientId) {
        return ResponseEntity.ok().body(appointmentService.getAppointmentsByPatientId(patientId));
    }

    @GetMapping("/appointments/doctor/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<List<AppointmentDto>> getAppointmentByDoctorId(@PathVariable Long id) {
        return ResponseEntity.ok().body(appointmentService.getAppointmentsByDoctorId(id));
    }


    @PostMapping("/appointment/add")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR' , 'PATIENT')")
    public ResponseEntity<AppointmentDto> addAppointment(@RequestBody @Valid AppointmentDto appointmentDto) throws URISyntaxException {
        return ResponseEntity.created(new URI("/appointment/add")).body(appointmentService.addAppointment(appointmentDto));
    }

    @PutMapping("/appointment/update")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR' , 'PATIENT')")
    public ResponseEntity<AppointmentDto> updateAppointment(@RequestBody AppointmentDto appointmentDto){
        return ResponseEntity.ok().body(appointmentService.updateAppointment(appointmentDto));
    }

    @DeleteMapping("/appointment/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR' , 'PATIENT')")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id){
        appointmentService.deleteAppointment(id);
        return ResponseEntity.ok().build();
    }





}
