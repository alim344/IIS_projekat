package com.example.autoskola.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "theory_exam")
public class TheoryExam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private LocalDate examDate;

    @Column(nullable = false)
    private LocalDateTime registrationDate;

    @ManyToOne
    @JoinColumn(name = "registered_by", nullable = false)
    private Professor registeredBy;

    @ManyToMany
    @JoinTable(
            name = "theory_exam_candidates",
            joinColumns = @JoinColumn(name = "exam_id"),
            inverseJoinColumns = @JoinColumn(name = "candidate_id")
    )
    private List<Candidate> candidates = new ArrayList<>();

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TheoryExamStatus status = TheoryExamStatus.REQUESTED;
}
