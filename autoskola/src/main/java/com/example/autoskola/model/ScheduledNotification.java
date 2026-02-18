package com.example.autoskola.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ScheduledNotification {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String text;


    @ManyToOne
    @JoinColumn(name = "candidate_id")
    private Candidate candidate;

}
