package com.spring.boot.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PrescriptionDto {
    private Long prescriptionId;
    private LocalDateTime dateIssued;
    private String notes;

    private Long doctorId;
    private String doctorName;

    private Long patientId;
    private String patientName;

    private List<Long> medicineIds;
    private List<String> medicineNames;
}
