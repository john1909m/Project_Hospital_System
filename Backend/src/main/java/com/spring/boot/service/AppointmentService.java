package com.spring.boot.service;

import com.spring.boot.dto.AppointmentDto;
import com.spring.boot.model.Appointment;

import java.util.List;

public interface AppointmentService {

    List<AppointmentDto> getAppointments();
    AppointmentDto getAppointmentById(Long id);
    AppointmentDto addAppointment(AppointmentDto appointmentDto);
    AppointmentDto updateAppointment(AppointmentDto appointmentDto);
    void deleteAppointment(Long id);
    List<AppointmentDto> getAppointmentsByDoctorId(Long doctorId);
    List<AppointmentDto> getAppointmentsByPatientId(Long patientId);
}
