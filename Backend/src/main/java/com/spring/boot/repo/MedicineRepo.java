package com.spring.boot.repo;

import com.spring.boot.model.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MedicineRepo extends JpaRepository<Medicine,Long> {
    Optional<Medicine> findMedicineByMedicineName(String name);

}
