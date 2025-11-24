package com.spring.boot.repo;

import com.spring.boot.model.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalRecordRepo extends JpaRepository<MedicalRecord,Long> {
    List<MedicalRecord> findByPatientPatientId(Long patientId);

    List<MedicalRecord> findByDoctorDoctorId(Long doctorId);
}
