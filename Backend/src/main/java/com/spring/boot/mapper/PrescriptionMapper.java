package com.spring.boot.mapper;

import com.spring.boot.dto.PrescriptionDto;
import com.spring.boot.model.Medicine;
import com.spring.boot.model.Prescription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PrescriptionMapper {
    @Mapping(source = "prescriptionDate", target = "dateIssued")
    @Mapping(source = "prescriptionNotes", target = "notes")
    @Mapping(source = "doctor.doctorId", target = "doctorId")
    @Mapping(source = "doctor.doctorName", target = "doctorName")
    @Mapping(source = "patient.patientId", target = "patientId")
    @Mapping(source = "patient.patientName", target = "patientName")
    @Mapping(source = "medicines", target = "medicineNames", qualifiedByName = "mapMedicinesToNames")
    @Mapping(source = "medicines", target = "medicineIds", qualifiedByName = "mapMedicinesToIds")
    PrescriptionDto toDto(Prescription prescription);

    List<PrescriptionDto> toDto(List<Prescription> prescriptions);

    @Mapping(source = "dateIssued", target = "prescriptionDate")
    @Mapping(source = "notes", target = "prescriptionNotes")
    Prescription toEntity(PrescriptionDto prescriptionDto);

    List<Prescription> toEntity(List<PrescriptionDto> prescriptionDtos);

    @Named("mapMedicinesToNames")
    default List<String> mapMedicinesToNames(List<Medicine> medicines) {
        if (medicines == null) return null;
        return medicines.stream().map(Medicine::getMedicineName).collect(Collectors.toList());
    }

    @Named("mapMedicinesToIds")
    default List<Long> mapMedicinesToIds(List<Medicine> medicines) {
        if (medicines == null) return null;
        return medicines.stream().map(Medicine::getMedicineId).collect(Collectors.toList());
    }

}
