package com.spring.boot.mapper;

import com.spring.boot.dto.MedicalRecordDto;
import com.spring.boot.dto.PrescriptionDto;
import com.spring.boot.model.MedicalRecord;
import com.spring.boot.model.Prescription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MedicalRecordMapper {
    MedicalRecordDto toDto(MedicalRecord medicalRecord);
    List<MedicalRecordDto> toDto(List<MedicalRecord> medicalRecords);
    MedicalRecord toEntity(MedicalRecordDto medicalRecordDto);
    List<MedicalRecord> toEntity(List<MedicalRecordDto> medicalRecordDtos);
}
