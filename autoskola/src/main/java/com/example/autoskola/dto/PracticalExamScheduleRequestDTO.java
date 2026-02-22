package com.example.autoskola.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class PracticalExamScheduleRequestDTO {
    private LocalDateTime dateTime; // spojeno date i time
    private Long candidateId;
    private Long instructorId;
    private Long suggestedProfessorId; // ako admin ručno izabere
    private boolean confirmSchedule;
}