package com.example.autoskola.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TheoryClassInfoDTO {

    public Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String lessonName;
    private int enrolledStudents;
    private int capacity;


    public TheoryClassInfoDTO() {
    }

    public TheoryClassInfoDTO(Long id, LocalDateTime startTime, LocalDateTime endTime, String lessonName, int enrolledStudents, int capacity) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.lessonName = lessonName;
        this.enrolledStudents = enrolledStudents;
        this.capacity = capacity;
    }
}
