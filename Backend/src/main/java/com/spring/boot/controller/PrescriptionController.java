package com.spring.boot.controller;

import com.spring.boot.dto.PrescriptionDto;
import com.spring.boot.service.PrescriptionService;
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
public class PrescriptionController {

    private PrescriptionService prescriptionService;

    @Autowired
    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }
    

    @GetMapping("/prescriptions")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<List<PrescriptionDto>> getAllPrescriptions() {
        return ResponseEntity.ok(prescriptionService.GetAllPrescriptions());
    }

    @GetMapping("/prescription/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<PrescriptionDto> getPrescriptionById(@PathVariable Long id) {
        return ResponseEntity.ok(prescriptionService.findPrescriptionById(id));
    }

    @GetMapping("/prescriptions/doctor/{doctorId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<List<PrescriptionDto>> getByDoctor(@PathVariable Long doctorId) {
        return ResponseEntity.ok(prescriptionService.findPrescriptionsByDoctorId(doctorId));
    }

    @GetMapping("/prescriptions/patient/{patientId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR' , 'PATIENT')")
    public ResponseEntity<List<PrescriptionDto>> getByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(prescriptionService.findPrescriptionsByPatientId(patientId));
    }

    @PostMapping("/prescription/add")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<PrescriptionDto> addPrescription(@RequestBody @Valid PrescriptionDto dto)
            throws URISyntaxException {
        return ResponseEntity.created(new URI("/prescription/add"))
                .body(prescriptionService.addPrescription(dto));
    }

    @PutMapping("/prescription/update")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<PrescriptionDto> updatePrescription(@RequestBody PrescriptionDto dto) {
        return ResponseEntity.ok(prescriptionService.updatePrescription(dto));
    }

    @DeleteMapping("/prescription/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<Void> deletePrescription(@PathVariable Long id) {
        prescriptionService.deletePrescription(id);
        return ResponseEntity.noContent().build();
    }
}
