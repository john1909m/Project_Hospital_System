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
public class AppointmentDto {


    private Long appointmentId;
    private Long doctorId;
    private String doctorName;
    private String patientName;
    private LocalDateTime appointmentDate;
    private String status;







}
