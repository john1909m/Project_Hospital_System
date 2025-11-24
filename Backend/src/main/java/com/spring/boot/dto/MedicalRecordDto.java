package com.spring.boot.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MedicalRecordDto {
    private Long recordId;
    private String diagnosis;
    private String notes;
    private LocalDateTime recordDate;

    private Long patientId;
    private String patientName;

    private Long doctorId;
    private String doctorName;
}
