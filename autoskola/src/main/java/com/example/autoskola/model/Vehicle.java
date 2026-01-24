package com.example.autoskola.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Vehicle {
    @Id
    private Long id;

    @OneToOne(mappedBy = "vehicle")
    private Instructor instructor;
}
