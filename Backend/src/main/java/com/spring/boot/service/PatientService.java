package com.spring.boot.service;

import com.spring.boot.dto.PatientDto;
import com.spring.boot.model.Patient;

import java.util.List;

public interface PatientService {
    List<PatientDto> getAllPatients();
    List<PatientDto> getAllPatientsByDoctorId(Long doctorId);
    PatientDto getPatientById(Long id);
    PatientDto getPatientByPatientName(String patientName);
    List<PatientDto> getAllPatientsByDoctorName(String doctorName);
    PatientDto addPatient(PatientDto patientDto);
    PatientDto updatePatient(PatientDto patientDto);
    void deletePatientById(Long id);
    Patient addPatientEntity(Patient patient);
}
