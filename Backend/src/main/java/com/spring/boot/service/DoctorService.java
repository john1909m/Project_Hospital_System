package com.spring.boot.service;

import com.spring.boot.dto.DoctorDto;
import com.spring.boot.dto.PatientDto;
import com.spring.boot.model.Appointment;
import com.spring.boot.model.Doctor;
import com.spring.boot.model.Patient;

import java.util.List;

public interface DoctorService {
    List<DoctorDto> getDoctors();

    DoctorDto addDoctor(DoctorDto doctorDto);

    Doctor addDoctorEntity(Doctor doctor);

    DoctorDto updateDoctor(DoctorDto doctorDto);

    void deleteDoctor(Long id);

    DoctorDto getDoctorById(Long id);

    DoctorDto getDoctorByName(String name);

    List<Appointment> getAppointmentsForDoctor(Long doctorId);

    List<PatientDto> getPatientsForDoctor(Long doctorId);
}
