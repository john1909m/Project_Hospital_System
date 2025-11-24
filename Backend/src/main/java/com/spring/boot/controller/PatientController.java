package com.spring.boot.controller;

import com.spring.boot.dto.PatientDto;
import com.spring.boot.service.PatientService;
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
public class PatientController {
    PatientService patientService;
    @Autowired
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping("/patients")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<List<PatientDto>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    @PostMapping("/patient/add")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR' , 'PATIENT')")
    public ResponseEntity<PatientDto> addPatient(@RequestBody @Valid PatientDto patientDto) throws URISyntaxException {
        return ResponseEntity.created(new URI("/patient/add")).body(patientService.addPatient(patientDto));
    }

    @PutMapping("/patient/update")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR' , 'PATIENT')")
    public ResponseEntity<PatientDto> updatePatient(@RequestBody @Valid PatientDto patientDto) {
        return ResponseEntity.ok(patientService.updatePatient(patientDto));
    }

    @DeleteMapping("/patient/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<PatientDto> deletePatient(@PathVariable @Valid Long id){
        patientService.deletePatientById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/patients/doctor/{doctorId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<List<PatientDto>> getAllPatientsOfDoctor(@PathVariable Long doctorId) {
        List<PatientDto> patients = patientService.getAllPatientsByDoctorId(doctorId);
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/patients/doctor/{doctorName}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR' ,)")
    public ResponseEntity<List<PatientDto>> getAllPatientsOfDoctor(@PathVariable String doctorName) {
        List<PatientDto> patients = patientService.getAllPatientsByDoctorName(doctorName);
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/patient/id/{patientId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<PatientDto> getPatientById(@PathVariable Long patientId) {
        return ResponseEntity.ok().body(patientService.getPatientById(patientId));
    }

    @GetMapping("/patient/name/{patientName}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<PatientDto> getAllPatientsByName(@PathVariable String patientName) {
        return ResponseEntity.ok().body(patientService.getPatientByPatientName(patientName));
    }

}
