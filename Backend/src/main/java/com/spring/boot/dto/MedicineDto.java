package com.spring.boot.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MedicineDto {

    private Long medicineId;

    private String medicineName;

    private String medicineDescription;

    private String medicinePrice;

    private String medicineCategory;
}
