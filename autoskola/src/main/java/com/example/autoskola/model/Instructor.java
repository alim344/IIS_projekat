package com.example.autoskola.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "instructors")
public class Instructor extends User{

    @OneToOne
    @JoinColumn(name = "vehicle_id", unique = true)
    private Vehicle vehicle;

    @OneToMany(mappedBy = "instructor")
    private List<InstructorDocuments> documents;
}
