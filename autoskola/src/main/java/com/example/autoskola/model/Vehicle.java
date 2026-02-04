package com.example.autoskola.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique = true)
    private String registrationNumber;

    @Column(nullable=false)
    private LocalDate registrationExpiryDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VehicleStatus status;

    @Column(nullable = false)
    private Integer currentMileage;

    @OneToOne
    @JoinColumn(name = "instructor_id", unique = true)
    private Instructor instructor;
}
