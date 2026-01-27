package com.example.autoskola.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CollectionId;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Candidate extends User{

    @Column
    private LocalDateTime startOfTraining;

    @OneToOne(mappedBy = "candidate")
    private TimePreference timePreference;

    @Column(nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "instructor_id")
    private Instructor instructor;  // nije dodeljen  na pocektu, bice null

    @Column
    private TrainingStatus status = TrainingStatus.THEORY;




}
