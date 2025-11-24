package com.spring.boot.repo;

import com.spring.boot.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepo extends JpaRepository<Appointment,Long> {
    List<Appointment> findByDoctorDoctorId(Long doctorId);
    List<Appointment> findByPatientPatientId(Long doctorId);

    List<Appointment> findByDoctorDoctorName(String doctorName);
    Optional<Appointment> findByDoctorDoctorIdAndAppointmentDate(Long doctorId, LocalDateTime appointmentDate);

    Optional<Appointment> findByAppointmentDate(LocalDateTime appointmentDate);


}
