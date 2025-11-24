package com.spring.boot.service;

import com.spring.boot.dto.PrescriptionDto;
import com.spring.boot.model.Prescription;

import java.util.List;

public interface PrescriptionService {
    List<PrescriptionDto> GetAllPrescriptions();
    PrescriptionDto findPrescriptionById(Long prescriptionId);

    List<PrescriptionDto> findPrescriptionsByPatientId(Long patientId);
    List<PrescriptionDto> findPrescriptionsByDoctorId(Long doctorId);

    PrescriptionDto addPrescription(PrescriptionDto prescriptionDto);
    PrescriptionDto updatePrescription(PrescriptionDto prescriptionDto);
    void deletePrescription(Long prescriptionId);
}
