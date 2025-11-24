package com.spring.boot.service;

import com.spring.boot.dto.MedicineDto;
import com.spring.boot.model.Medicine;

import java.util.List;

public interface MedicineService {
    List<MedicineDto> getMedicines();

    MedicineDto getMedicineById(Long id);

    MedicineDto getMedicineByName(String name);

    MedicineDto addMedicine(MedicineDto medicineDto);

    MedicineDto updateMedicine(MedicineDto medicineDto);

    void deleteMedicine(Long id);
}
