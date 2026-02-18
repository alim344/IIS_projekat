package com.example.autoskola.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CandidateTheoryClassDTO {

    private Long id;
    private String title;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int capacity;
    private int enrolledStudents;
    private boolean isEnrolled;
    private String professorName;
    private String professorLastName;


    public CandidateTheoryClassDTO() {
    }

    public CandidateTheoryClassDTO(Long id, String title, LocalDateTime startTime, LocalDateTime endTime, int capacity,
                                   int enrolledStudents, boolean isEnrolled, String professorName, String professorLastName) {
        this.id = id;
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.capacity = capacity;
        this.enrolledStudents = enrolledStudents;
        this.isEnrolled = isEnrolled;
        this.professorName = professorName;
        this.professorLastName = professorLastName;
    }
}
