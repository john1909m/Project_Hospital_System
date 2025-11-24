package com.spring.boot.service.impl;

import com.spring.boot.dto.MedicineDto;
import com.spring.boot.mapper.MedicineMapper;
import com.spring.boot.model.Medicine;
import com.spring.boot.repo.MedicineRepo;
import com.spring.boot.service.MedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MedicineServiceImpl implements MedicineService {

    private MedicineRepo medicineRepo;
    private MedicineMapper medicineMapper;

    @Autowired
    public MedicineServiceImpl(MedicineRepo medicineRepo, MedicineMapper medicineMapper) {
        this.medicineRepo = medicineRepo;
        this.medicineMapper = medicineMapper;

    }



    @Override
    public List<MedicineDto> getMedicines() {
        List<Medicine> medicines = medicineRepo.findAll();
        return medicines.stream().map(medicine ->
                medicineMapper.toDto(medicine))
                .collect(Collectors.toList());
    }

    @Override
    public MedicineDto getMedicineById(Long id) {
        Optional<Medicine> medicine = medicineRepo.findById(id);
        if (medicine.isEmpty()) {
            throw new RuntimeException("Medicine.id.notExist");
        }
        return medicineMapper.toDto(medicine.get());
    }

    @Override
    public MedicineDto getMedicineByName(String name) {
        Optional<Medicine> medicineOptional=medicineRepo.findMedicineByMedicineName(name);
        if (medicineOptional.isEmpty()) {
            throw new RuntimeException("Medicine.name.notExist");
        }
        return medicineMapper.toDto(medicineOptional.get());
    }

    @Override
    public MedicineDto addMedicine(MedicineDto medicineDto) {
        if(Objects.nonNull(medicineDto.getMedicineId())){
            throw new RuntimeException("Medicine.id.notRequired");
        }
        if (Objects.isNull(medicineDto.getMedicineName())) {
            throw new RuntimeException("Medicine.name.required");
        }
        if (Objects.isNull(medicineDto.getMedicinePrice())) {
            throw new RuntimeException("Medicine.price.required");
        }
        if (Objects.isNull(medicineDto.getMedicineCategory())) {
            throw new RuntimeException("Medicine.category.required");
        }
        Optional<Medicine> medicineOptional=medicineRepo.findMedicineByMedicineName(medicineDto.getMedicineName());
        if (medicineOptional.isPresent()) {
            throw new RuntimeException("Medicine.name.exist");
        }
        Medicine medicine=medicineRepo.save(medicineMapper.toEntity(medicineDto));
        return medicineDto;

    }

    @Override
    public MedicineDto updateMedicine(MedicineDto medicineDto) {
        if(Objects.isNull(medicineDto.getMedicineId())){
            throw new RuntimeException("Medicine.id.required");
        }
        medicineRepo.save(medicineMapper.toEntity(medicineDto));
        return medicineDto;
    }

    @Override
    public void deleteMedicine(Long id) {
        Optional<Medicine> medicineOptional=medicineRepo.findById(id);
        if (medicineOptional.isEmpty()) {
            throw new RuntimeException("Medicine.id.notExist");
        }
        medicineRepo.deleteById(id);
    }
}
