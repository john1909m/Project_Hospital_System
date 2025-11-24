package com.spring.boot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Medicine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long medicineId;

    @Column(nullable = false)
    private String medicineName;

    private String medicineDescription;

    @Column(nullable = false)
    private String medicinePrice;

    private String medicineCategory;

    @ManyToMany(mappedBy = "medicines")
    private List<Prescription> prescriptions;
}
