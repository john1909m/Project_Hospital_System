package com.spring.boot.mapper;

import com.spring.boot.dto.AppointmentDto;
import com.spring.boot.dto.PatientDto;
import com.spring.boot.model.Appointment;
import com.spring.boot.model.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
@Mapper(componentModel = "spring")
public interface PatientMapper {

    Patient toEntity(PatientDto patientDto);

    PatientDto toDto(Patient patient);

    List<PatientDto> toDto(List<Patient> patients);
    List<Patient> toEntity(List<PatientDto> patientDtos);
}
