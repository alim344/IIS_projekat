package com.example.autoskola.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CollectionId;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @ManyToMany
    @JoinTable(
            name = "candidate_attended_lessons",
            joinColumns = @JoinColumn(name = "candidate_id"),
            inverseJoinColumns = @JoinColumn(name = "lesson_id")
    )
    private Set<TheoryLesson> attendedLessons = new HashSet<>();

    @ManyToMany(mappedBy = "students")
    private List<TheoryClass> scheduledClasses = new ArrayList<>();


}
