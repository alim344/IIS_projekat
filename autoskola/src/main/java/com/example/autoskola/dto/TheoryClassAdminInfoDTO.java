package com.example.autoskola.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class TheoryClassAdminInfoDTO {
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer capacity;
    private Integer enrolledStudents;
    private TheoryLessonSimpleDTO theoryLesson;
    private ProfessorDTO professor;
    private List<CandidateSimpleDTO> students;

    public TheoryClassAdminInfoDTO() {}
}