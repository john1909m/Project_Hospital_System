package com.spring.boot.service.impl;

import com.spring.boot.dto.AppointmentDto;
import com.spring.boot.mapper.AppointmentMapper;
import com.spring.boot.model.Appointment;
import com.spring.boot.model.Doctor;
import com.spring.boot.model.Patient;
import com.spring.boot.repo.AppointmentRepo;
import com.spring.boot.repo.DoctorRepo;
import com.spring.boot.repo.PatientRepo;
import com.spring.boot.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private AppointmentRepo appointmentRepo;
    private DoctorRepo doctorRepo;
    private PatientRepo patientRepo;
    private AppointmentMapper appointmentMapper;

    @Autowired
    public AppointmentServiceImpl(AppointmentRepo appointmentRepo,PatientRepo patientRepo,DoctorRepo doctorRepo,AppointmentMapper appointmentMapper) {
        this.appointmentRepo = appointmentRepo;
        this.doctorRepo = doctorRepo;
        this.appointmentMapper = appointmentMapper;
        this.patientRepo = patientRepo;
    }

    @Override
    public List<AppointmentDto> getAppointments() {
        List<Appointment> appointments = appointmentRepo.findAll();
        return appointments.stream().map(appointment ->
                appointmentMapper.toDto(appointment)).collect(Collectors.toList());
    }

    @Override
    public AppointmentDto getAppointmentById(Long id) {
        Optional<Appointment> appointmentOptional = appointmentRepo.findById(id);
        if (appointmentOptional.isEmpty()){
            throw new RuntimeException("appointment.id.notExist");
        }
        return appointmentMapper.toDto(appointmentOptional.get());
    }

    @Override
    public AppointmentDto addAppointment(AppointmentDto appointmentDto) {
        if (Objects.nonNull(appointmentDto.getAppointmentId())) {
            throw new RuntimeException("appointment.id.notRequired");
        }
        if (Objects.isNull(appointmentDto.getAppointmentDate())) {
            throw new RuntimeException("appointment.date.Required");
        }
        if (Objects.isNull(appointmentDto.getDoctorName())) {
            throw new RuntimeException("appointment.doctor.name.Required");
        }
        if (Objects.isNull(appointmentDto.getPatientName())) {
            throw new RuntimeException("appointment.patient.name.Required");
        }

        Doctor doctor = doctorRepo.findDoctorByDoctorName(appointmentDto.getDoctorName())
                .orElseThrow(() -> new RuntimeException("doctor.name.notFound"));
        Patient patient = patientRepo.findPatientByPatientName(appointmentDto.getPatientName())
                .orElseThrow(() -> new RuntimeException("patient.name.notFound"));

        Appointment appointment = appointmentMapper.toEntity(appointmentDto);

        appointment.setDoctor(doctor);
        appointment.setPatient(patient);

        Optional<Appointment> appointmentOptional =
                appointmentRepo.findByDoctorDoctorIdAndAppointmentDate(
                        doctor.getDoctorId(), appointmentDto.getAppointmentDate()
                );


        if (appointmentOptional.isPresent()) {
            throw new RuntimeException("doctor.notAvailableAtThisTime");
        }


        return appointmentMapper.toDto(appointmentRepo.save(appointment));
    }


    @Override
    public AppointmentDto updateAppointment(AppointmentDto appointmentDto) {
        if(Objects.isNull(appointmentDto.getAppointmentId())){
            throw new RuntimeException("appointment.id.Required");
        }

        Appointment existingAppointment = appointmentRepo.findById(appointmentDto.getAppointmentId())
                .orElseThrow(() -> new RuntimeException("appointment.id.notFound"));

        Doctor doctor = doctorRepo.findDoctorByDoctorName(appointmentDto.getDoctorName())
                .orElseThrow(() -> new RuntimeException("Doctor not found with this name"));

        if (!existingAppointment.getAppointmentDate().equals(appointmentDto.getAppointmentDate())) {
            Optional<Appointment> conflictingAppointment =
                    appointmentRepo.findByDoctorDoctorIdAndAppointmentDate(
                            doctor.getDoctorId(), appointmentDto.getAppointmentDate()
                    );

            if (conflictingAppointment.isPresent()) {
                throw new RuntimeException("doctor.notAvailableAtThisTime");
            }

            existingAppointment.setAppointmentDate(appointmentDto.getAppointmentDate());
        }
        existingAppointment.setPatientName(appointmentDto.getPatientName());
        existingAppointment.setStatus(appointmentDto.getStatus());
        existingAppointment.setDoctor(doctor);
        Optional<Appointment> appointmentOptional = appointmentRepo.findByAppointmentDate(appointmentDto.getAppointmentDate());

        Appointment updatedAppointment = appointmentRepo.save(existingAppointment);

        return appointmentMapper.toDto(updatedAppointment);
    }

    @Override
    public void deleteAppointment(Long id) {
        Optional<Appointment> appointmentOptional = appointmentRepo.findById(id);
        if (appointmentOptional.isEmpty()){
            throw new RuntimeException("appointment.id.notExist");
        }

        appointmentRepo.deleteById(id);
    }

    @Override
    public List<AppointmentDto> getAppointmentsByDoctorId(Long doctorId) {
        List<Appointment> appointments=appointmentRepo.findByDoctorDoctorId(doctorId);

        return appointments.stream().map(appointmentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentDto> getAppointmentsByPatientId(Long patientId) {
        List<Appointment> appointments=appointmentRepo.findByPatientPatientId(patientId);
        return appointments.stream().map(appointmentMapper::toDto)
                .collect(Collectors.toList());
    }

}
