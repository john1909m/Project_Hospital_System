package com.spring.boot.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PatientDto {
    private Long patientId;

    @NotBlank
    private String patientName;


    @NotBlank
    private String patientPhone;

    @NotBlank
    private String patientGender;

    private int patientAge;

    private Long userId;
}
