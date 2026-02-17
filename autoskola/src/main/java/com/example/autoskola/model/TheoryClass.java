package com.example.autoskola.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class TheoryClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime startTime;
    @Column
    private LocalDateTime endTime;

    @ManyToOne
    @JoinColumn(name = "professor_id",nullable = false)
    private Professor professor;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int capacity;

    @Column(nullable = false)
    private int enrolledStudents;

    @ManyToOne
    @JoinColumn(name = "lesson_id",nullable = false)
    private TheoryLesson theoryLesson;


}
