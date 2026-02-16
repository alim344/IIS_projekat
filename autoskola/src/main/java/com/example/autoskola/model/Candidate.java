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
    @Enumerated(EnumType.STRING)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "instructor_id")
    private Instructor instructor;  // nije dodeljen  na pocektu, bice null

    @Column
    @Enumerated(EnumType.STRING)
    private TrainingStatus status = TrainingStatus.THEORY;

    @Column(name = "preferred_location")
    private String preferredLocation;

}
