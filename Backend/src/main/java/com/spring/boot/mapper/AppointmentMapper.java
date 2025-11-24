package com.spring.boot.mapper;

import com.spring.boot.dto.AppointmentDto;
import com.spring.boot.dto.DoctorDto;
import com.spring.boot.model.Appointment;
import com.spring.boot.model.Doctor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
@Mapper(componentModel = "spring")
public interface AppointmentMapper {
    @Mapping(target = "doctor", ignore = true)
    Appointment toEntity(AppointmentDto appointmentDto);

    @Mapping(source = "doctor.doctorName", target = "doctorName")
    AppointmentDto toDto(Appointment appointment);

    List<AppointmentDto> toDto(List<Appointment> appointments);
    List<Appointment> toEntity(List<AppointmentDto> appointmentDtos);
}
