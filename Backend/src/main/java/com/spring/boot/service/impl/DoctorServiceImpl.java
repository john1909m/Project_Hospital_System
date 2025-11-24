package com.spring.boot.service.impl;

import com.spring.boot.dto.DoctorDto;
import com.spring.boot.dto.PatientDto;
import com.spring.boot.mapper.DoctorMapper;
import com.spring.boot.mapper.PatientMapper;
import com.spring.boot.model.Appointment;
import com.spring.boot.model.Doctor;
import com.spring.boot.model.Patient;
import com.spring.boot.repo.AppointmentRepo;
import com.spring.boot.repo.DoctorRepo;
import com.spring.boot.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DoctorServiceImpl implements DoctorService {

    private AppointmentRepo appointmentRepo;
    private DoctorRepo doctorRepo;
    private DoctorMapper doctorMapper;
    private PatientMapper patientMapper;

    @Autowired
    public DoctorServiceImpl(DoctorRepo doctorRepo,PatientMapper patientMapper, DoctorMapper doctorMapper, AppointmentRepo appointmentRepo) {
        this.doctorRepo = doctorRepo;
        this.doctorMapper = doctorMapper;
        this.appointmentRepo = appointmentRepo;
        this.patientMapper = patientMapper;
    }

    @Override
    public List<DoctorDto> getDoctors() {
        List<Doctor> doctors = doctorRepo.findAll();
        return doctors.stream().map(doctor ->
                doctorMapper.toDto(doctor)).collect(Collectors.toList());
    }

    @Override
    public DoctorDto addDoctor(DoctorDto doctorDto) {
        if(Objects.nonNull(doctorDto.getDoctorId())){
            throw new RuntimeException(("doctor.id.notRequired"));
        }
        if(Objects.isNull(doctorDto.getDoctorName())){
            throw new RuntimeException(("doctor.name.Required"));
        }
        if(Objects.isNull(doctorDto.getDoctorPhone())){
            throw new RuntimeException(("doctor.phone.Required"));
        }
        Optional<Doctor> doctorOptional = doctorRepo.findDoctorByDoctorName(doctorDto.getDoctorName());
        if(doctorOptional.isPresent()){
            throw new RuntimeException(("doctor.name.exists"));
        }
        Doctor doctor=doctorRepo.save(doctorMapper.toEntity(doctorDto));
        return doctorDto;
    }

    @Override
    public Doctor addDoctorEntity(Doctor doctor) {
        return doctorRepo.save(doctor);
    }

    @Override
    public DoctorDto updateDoctor(DoctorDto doctorDto) {
        if(Objects.isNull(doctorDto.getDoctorId())){
            throw new RuntimeException(("doctor.id.Required"));
        }
        doctorRepo.save(doctorMapper.toEntity(doctorDto));
        return doctorDto;
    }

    @Override
    public void deleteDoctor(Long id) {
        Optional<Doctor> doctorOptional=doctorRepo.findById(id);
        if(doctorOptional.isEmpty()){
            throw new RuntimeException(("doctor.id.notExists"));
        }
        doctorRepo.deleteById(id);
    }

    @Override
    public DoctorDto getDoctorById(Long id) {
        Optional<Doctor> doctorOptional=doctorRepo.findById(id);
        if(doctorOptional.isEmpty()){
            throw new RuntimeException(("doctor.id.notExists"));
        }
        return doctorMapper.toDto(doctorOptional.get());
    }

    @Override
    public DoctorDto getDoctorByName(String name) {
        Optional<Doctor> doctorOptional=doctorRepo.findDoctorByDoctorName(name);
        if(doctorOptional.isEmpty()){
            throw new RuntimeException(("doctor.name.notExists"));
        }
        return doctorMapper.toDto(doctorOptional.get());
    }

    @Override
    public List<Appointment> getAppointmentsForDoctor(Long doctorId) {
        return appointmentRepo.findByDoctorDoctorId(doctorId);
    }

    @Override
    public List<PatientDto> getPatientsForDoctor(Long doctorId) {
        List<Patient> patients=appointmentRepo.findByDoctorDoctorId(doctorId)
                .stream().map(appointment ->
                        appointment.getPatient())
                .distinct().collect(Collectors.toList());
        return patientMapper.toDto(patients) ;
    }
}
