package com.spring.boot.service;

import com.spring.boot.dto.MedicalRecordDto;

import java.util.List;

public interface MedicalRecordService {
    List<MedicalRecordDto> getAllRecords();

    MedicalRecordDto getRecordById(Long recordId);

    List<MedicalRecordDto> getRecordsByPatientId(Long patientId);

    List<MedicalRecordDto> getRecordsByDoctorId(Long doctorId);

    MedicalRecordDto addRecord(MedicalRecordDto medicalRecordDto);

    MedicalRecordDto updateRecord(MedicalRecordDto medicalRecordDto);

    void deleteRecord(Long recordId);
}
