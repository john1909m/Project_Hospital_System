package com.spring.boot.service.impl;

import com.spring.boot.dto.MedicalRecordDto;
import com.spring.boot.mapper.MedicalRecordMapper;
import com.spring.boot.model.Doctor;
import com.spring.boot.model.MedicalRecord;
import com.spring.boot.model.Patient;
import com.spring.boot.repo.DoctorRepo;
import com.spring.boot.repo.MedicalRecordRepo;
import com.spring.boot.repo.MedicineRepo;
import com.spring.boot.repo.PatientRepo;
import com.spring.boot.service.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private MedicalRecordRepo medicalRecordRepo;
    private MedicalRecordMapper medicalRecordMapper;
    private DoctorRepo doctorRepo;
    private PatientRepo patientRepo;


    @Autowired
    public MedicalRecordServiceImpl(MedicalRecordRepo medicalRecordRepo, MedicalRecordMapper medicalRecordMapper, DoctorRepo doctorRepository, PatientRepo patientRepository) {
        this.medicalRecordRepo = medicalRecordRepo;
        this.medicalRecordMapper = medicalRecordMapper;
        this.doctorRepo = doctorRepository;
        this.patientRepo = patientRepository;
    }

    @Override
    public List<MedicalRecordDto> getAllRecords() {
        List<MedicalRecord> medicalRecords = medicalRecordRepo.findAll();
        return medicalRecords.stream().map(medicalRecord ->
                medicalRecordMapper.toDto(medicalRecord))
                .collect(Collectors.toList());
    }

    @Override
    public MedicalRecordDto getRecordById(Long recordId) {
        Optional<MedicalRecord> medicalRecord = medicalRecordRepo.findById(recordId);
        if (medicalRecord.isEmpty()) {
            throw new RuntimeException("Medical.Record.NotFound");
        }
        return medicalRecordMapper.toDto(medicalRecord.get());
    }

    @Override
    public List<MedicalRecordDto> getRecordsByPatientId(Long patientId) {
        return medicalRecordRepo.findByPatientPatientId(patientId)
                .stream().map(medicalRecord ->
                        medicalRecordMapper.toDto(medicalRecord))
                .collect(Collectors.toList());
    }

    @Override
    public List<MedicalRecordDto> getRecordsByDoctorId(Long doctorId) {
        return medicalRecordRepo.findByDoctorDoctorId(doctorId)
                .stream().map(medicalRecord ->
                        medicalRecordMapper.toDto(medicalRecord))
                .collect(Collectors.toList());
    }

    @Override
    public MedicalRecordDto addRecord(MedicalRecordDto medicalRecordDto) {
        if (Objects.nonNull(medicalRecordDto.getRecordId())) {
            throw new RuntimeException("Medical.Record.id.notRequired");

        }
        if (Objects.isNull(medicalRecordDto.getPatientId())) {
            throw new RuntimeException("Medical.Record.patientId.Required");
        }
        if (Objects.isNull(medicalRecordDto.getDoctorId())) {
            throw new RuntimeException("Medical.Record.doctorId.Required");
        }
        Doctor doctor = doctorRepo.findById(medicalRecordDto.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        Patient patient = patientRepo.findById(medicalRecordDto.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        MedicalRecord medicalRecord=medicalRecordMapper.toEntity(medicalRecordDto);
        medicalRecord.setDoctor(doctor);
        medicalRecord.setPatient(patient);
        if (medicalRecord.getRecordDate() == null) {
            medicalRecord.setRecordDate(LocalDateTime.now());
        }
        medicalRecordRepo.save(medicalRecord);
        return medicalRecordDto;
    }

    @Override
    public MedicalRecordDto updateRecord(MedicalRecordDto medicalRecordDto) {
        if (Objects.isNull(medicalRecordDto.getRecordId())) {
            throw new RuntimeException("Medical.Record.id.Required");
        }
        medicalRecordRepo.save(medicalRecordMapper.toEntity(medicalRecordDto));
        return medicalRecordDto;
    }

    @Override
    public void deleteRecord(Long recordId) {
        Optional<MedicalRecord> medicalRecord = medicalRecordRepo.findById(recordId);
        if (medicalRecord.isEmpty()) {
            throw new RuntimeException("Medical.Record.NotFound");
        }
        medicalRecordRepo.deleteById(recordId);
    }
}
