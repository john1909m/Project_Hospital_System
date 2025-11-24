package com.spring.boot.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
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
public class DoctorDto {

    private Long doctorId;

    @NotBlank
    private String doctorName;

    @NotBlank
    private String doctorPhone;

    @NotBlank
    private String doctorDepartment;

    private Long userId;

}
