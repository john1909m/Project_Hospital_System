package com.spring.boot.controller;

import com.spring.boot.dto.MedicalRecordDto;
import com.spring.boot.model.MedicalRecord;
import com.spring.boot.service.MedicalRecordService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5174/")

@RestController
public class MedicalRecordController {

    public MedicalRecordService medicalRecordService;

    @Autowired
    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    @GetMapping("/medical-records")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<List<MedicalRecordDto>> getAllRecords() {
        return ResponseEntity.ok().body(medicalRecordService.getAllRecords());
    }

    @PostMapping("/medical-record/add")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<MedicalRecordDto> addRecord(@RequestBody @Valid MedicalRecordDto medicalRecordDto) {
        return ResponseEntity.ok().body(medicalRecordService.addRecord(medicalRecordDto));
    }

    @PutMapping("/medical-record/update")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<MedicalRecordDto> updateRecord(@RequestBody MedicalRecordDto medicalRecordDto) {
        return ResponseEntity.ok().body(medicalRecordService.updateRecord(medicalRecordDto));
    }

    @DeleteMapping("/medical-record/delete/{recordId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<MedicalRecordDto> deleteRecord(@PathVariable Long recordId) {
        medicalRecordService.deleteRecord(recordId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/medical-record/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR' , 'PATIENT')")
    public ResponseEntity<MedicalRecordDto> getRecord(@PathVariable Long id) {
        return ResponseEntity.ok().body(medicalRecordService.getRecordById(id));
    }

    @GetMapping("/medical-record/patient/{patientId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR' , 'PATIENT')")
    public ResponseEntity<List<MedicalRecordDto>> getPatientRecords(@PathVariable Long patientId) {
        return ResponseEntity.ok().body(medicalRecordService.getRecordsByPatientId(patientId));
    }

    @GetMapping("/medical-record/doctor/{doctorId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<List<MedicalRecordDto>> getDoctorRecords(@PathVariable Long doctorId) {
        return ResponseEntity.ok().body(medicalRecordService.getRecordsByDoctorId(doctorId));
    }





}
