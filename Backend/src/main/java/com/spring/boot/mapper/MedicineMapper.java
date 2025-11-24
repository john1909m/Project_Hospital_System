package com.spring.boot.mapper;

import com.spring.boot.dto.DoctorDto;
import com.spring.boot.dto.MedicineDto;
import com.spring.boot.model.Doctor;
import com.spring.boot.model.Medicine;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
@Mapper(componentModel = "spring")
public interface MedicineMapper {

    Medicine toEntity(MedicineDto medicineDto);

    MedicineDto toDto(Medicine medicine);
    List<MedicineDto> toDto(List<Medicine> medicines);
    List<Medicine> toEntity(List<MedicineDto> medicineDtos);
}
