package com.example.autoskola.model;

import jakarta.persistence.*;
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

    @OneToMany(mappedBy = "instructor", fetch = FetchType.LAZY)
    private List<Candidate> candidates;

    @Column(nullable = false)
    private Integer maxCapacity;

}
