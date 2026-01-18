package com.example.autoskola.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Entity
public class TimePreference {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "candidate_id", referencedColumnName = "id")
    private Candidate candidate;

}
