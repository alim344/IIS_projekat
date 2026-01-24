package com.example.autoskola.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Instructor extends User{



    @OneToOne
    @JoinColumn(name = "vehicle_id", unique = true)
    private Vehicle vehicle;







}
