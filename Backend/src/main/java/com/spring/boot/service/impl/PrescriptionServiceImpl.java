package com.spring.boot.service.impl;

import com.spring.boot.dto.PrescriptionDto;
import com.spring.boot.mapper.PrescriptionMapper;
import com.spring.boot.model.Doctor;
import com.spring.boot.model.Medicine;
import com.spring.boot.model.Patient;
import com.spring.boot.model.Prescription;
import com.spring.boot.repo.DoctorRepo;
import com.spring.boot.repo.MedicineRepo;
import com.spring.boot.repo.PatientRepo;
import com.spring.boot.repo.PrescriptionRepo;
import com.spring.boot.service.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PrescriptionServiceImpl implements PrescriptionService {

    private PrescriptionRepo prescriptionRepo;
    private DoctorRepo doctorRepo;
    private PatientRepo patientRepo;
    private PrescriptionMapper prescriptionMapper;
    private MedicineRepo medicineRepo;

    @Autowired
    public PrescriptionServiceImpl(PrescriptionRepo prescriptionRepo,MedicineRepo medicineRepo,PrescriptionMapper prescriptionMapper,DoctorRepo doctorRepo,PatientRepo patientRepo) {
        this.prescriptionRepo = prescriptionRepo;
        this.prescriptionMapper = prescriptionMapper;
        this.doctorRepo= doctorRepo;
        this.patientRepo = patientRepo;
        this.medicineRepo = medicineRepo;
    }
    @Override
    public List<PrescriptionDto> GetAllPrescriptions() {
        List<Prescription> prescriptions = prescriptionRepo.findAll();
        return prescriptions.stream().map(prescription ->
                prescriptionMapper.toDto(prescription)).collect(Collectors.toList());
    }

    @Override
    public PrescriptionDto findPrescriptionById(Long prescriptionId) {
        Optional<Prescription> prescription = prescriptionRepo.findById(prescriptionId);
        if (prescription.isEmpty()) {
            throw new RuntimeException("Prescription.id.notFound");
        }
        return prescriptionMapper.toDto(prescription.get());

    }

    @Override
    public List<PrescriptionDto> findPrescriptionsByPatientId(Long patientId) {
        return prescriptionRepo.findByPatientPatientId(patientId)
                .stream().map(prescription -> prescriptionMapper.toDto(prescription)).collect(Collectors.toList());
    }

    @Override
    public List<PrescriptionDto> findPrescriptionsByDoctorId(Long doctorId) {
        return prescriptionRepo.findByDoctorDoctorId(doctorId)
                .stream().map(prescription -> prescriptionMapper.toDto(prescription)).collect(Collectors.toList());
    }

    @Override
    public PrescriptionDto addPrescription(PrescriptionDto prescriptionDto) {
        Doctor doctor = doctorRepo.findById(prescriptionDto.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        Patient patient = patientRepo.findById(prescriptionDto.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        List<Medicine> medicines = medicineRepo.findAllById(prescriptionDto.getMedicineIds());

        if (prescriptionDto.getPrescriptionId() != null) {
            throw new RuntimeException("Prescription.id.notRequired");
        }
        if (prescriptionDto.getPatientId() == null) {
            throw new RuntimeException("Patient.id.Required");
        }
        if (prescriptionDto.getDoctorId() == null) {
            throw new RuntimeException("Doctor.id.Required");
        }
        if (prescriptionDto.getMedicineNames() == null) {
            throw new RuntimeException("Medicine.names.Required");
        }

        Prescription prescription=prescriptionMapper.toEntity(prescriptionDto);

        if (prescriptionDto.getDateIssued() != null) {
            prescription.setPrescriptionDate(prescriptionDto.getDateIssued());
        } else {
            prescription.setPrescriptionDate(LocalDateTime.now());
        }

        prescription.setDoctor(doctor);
        prescription.setPatient(patient);
        prescription.setMedicines(medicines);
        prescriptionRepo.save(prescription);
        return prescriptionDto;
    }

    @Override
    public PrescriptionDto updatePrescription(PrescriptionDto prescriptionDto) {
        if (prescriptionDto.getPrescriptionId() == null) {
            throw new RuntimeException("Prescription.id.Required");
        }
        Prescription existingPrescription = prescriptionRepo.findById(prescriptionDto.getPrescriptionId())
                .orElseThrow(() -> new RuntimeException("prescription.notFound"));

        Doctor doctor = doctorRepo.findById(prescriptionDto.getDoctorId())
                .orElseThrow(() -> new RuntimeException("doctor.notFound"));
        Patient patient = patientRepo.findById(prescriptionDto.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        Prescription updatedPrescription = prescriptionMapper.toEntity(prescriptionDto);
        updatedPrescription.setDoctor(doctor);
        updatedPrescription.setPatient(patient);

        prescriptionRepo.save(updatedPrescription);

        return prescriptionDto;
    }

    @Override
    public void deletePrescription(Long prescriptionId) {
        Optional<Prescription> prescriptionOptional = prescriptionRepo.findById(prescriptionId);
        if (prescriptionOptional.isEmpty()) {
            throw new RuntimeException("Prescription.id.notExist");
        }
        prescriptionRepo.deleteById(prescriptionId);
    }
}
