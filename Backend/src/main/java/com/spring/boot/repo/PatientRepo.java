package com.spring.boot.repo;

import com.spring.boot.dto.PatientDto;
import com.spring.boot.model.Doctor;
import com.spring.boot.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepo extends JpaRepository<Patient,Long> {
    Optional<Patient> findPatientByPatientName(String patientName);


}
