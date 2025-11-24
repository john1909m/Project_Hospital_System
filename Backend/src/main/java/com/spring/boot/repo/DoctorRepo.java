package com.spring.boot.repo;

import com.spring.boot.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorRepo extends JpaRepository<Doctor,Long> {
    Optional<Doctor> findDoctorByDoctorName(String doctorName);
}
