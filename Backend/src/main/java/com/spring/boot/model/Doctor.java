package com.spring.boot.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long doctorId;

    @Column(unique = true, nullable = false)
    private String doctorName;

    @Column(unique = true, nullable = false,name = "DOCTOR_PHONE")
    private String doctorPhone;

    @Column(nullable = false)
    private String doctorDepartment;

    @OneToMany(mappedBy = "doctor")
    @JsonManagedReference
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Prescription> prescriptions;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    private List<MedicalRecord> medicalRecords;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}
