package com.spring.boot.mapper;

import com.spring.boot.dto.DoctorDto;
import com.spring.boot.model.Doctor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DoctorMapper {
    @Mapping(source = "doctorId", target = "doctorId")
    Doctor toEntity(DoctorDto doctorDto);

    @Mapping(source = "doctorId", target = "doctorId")
    DoctorDto toDto(Doctor doctor);
    List<DoctorDto> toDto(List<Doctor> doctors);
    List<Doctor> toEntity(List<DoctorDto> doctorDtos);
}
