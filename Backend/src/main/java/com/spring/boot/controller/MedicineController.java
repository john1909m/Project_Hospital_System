package com.spring.boot.controller;

import com.spring.boot.dto.MedicineDto;
import com.spring.boot.service.MedicineService;
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
public class MedicineController {
    public MedicineService medicineService;

    @Autowired
    public MedicineController(MedicineService medicineService) {
        this.medicineService = medicineService;
    }

    @GetMapping("/medicines")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR' , 'PATIENT')")
    public ResponseEntity<List<MedicineDto>> getMedicines() {
        return ResponseEntity.ok().body(medicineService.getMedicines());
    }

    @PostMapping("/medicine/add")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<MedicineDto> addMedicine(@RequestBody @Valid MedicineDto medicineDto) throws URISyntaxException {
        return ResponseEntity.created(new URI("/medicine/add")).body(medicineService.addMedicine(medicineDto));
    }

    @PutMapping("/medicine/update")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<MedicineDto> updateMedicine(@RequestBody @Valid MedicineDto medicineDto) {
        return ResponseEntity.ok().body(medicineService.updateMedicine(medicineDto));
    }

    @DeleteMapping("/medicine/delete/{medicineId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Void> deleteMedicine(@PathVariable Long medicineId) {
        medicineService.deleteMedicine(medicineId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/medicine/id/{medicineId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR' , 'PATIENT')")
    public ResponseEntity<MedicineDto> getMedicine(@PathVariable Long medicineId) {
        return ResponseEntity.ok().body(medicineService.getMedicineById(medicineId));
    }

    @GetMapping("/medicine/name/{medicineName}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR' , 'PATIENT')")
    public ResponseEntity<MedicineDto> getMedicineByName(@PathVariable String medicineName) {
        return ResponseEntity.ok().body(medicineService.getMedicineByName(medicineName));
    }
}
