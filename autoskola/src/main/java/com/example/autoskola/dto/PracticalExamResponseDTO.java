package com.example.autoskola.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class PracticalExamResponseDTO {
    private Long id;
    private LocalDateTime dateTime;
    private String status;
    private Long candidateId;
    private String candidateName;
    private String candidateLastName;
    private Long instructorId;
    private String instructorName;
    private String instructorLastName;
    private Long professorId;
    private String professorName;
    private String professorLastName;
}