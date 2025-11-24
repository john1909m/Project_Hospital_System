package com.spring.boot.repo;

import com.spring.boot.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionRepo extends JpaRepository<Prescription,Long> {
    List<Prescription> findByDoctorDoctorId(Long doctorId);
    List<Prescription> findByPatientPatientId(Long patientId);


}
