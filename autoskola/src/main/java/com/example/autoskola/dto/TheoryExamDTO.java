package com.example.autoskola.dto;

import com.example.autoskola.model.TheoryExam;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class TheoryExamDTO {
    private Long id;
    private LocalDate examDate;
    private LocalDateTime registrationDate;
    private String registeredByName;
    private String status;
    private Integer totalCandidates;
    private List<CandidateSimpleDTO> candidates;

    public TheoryExamDTO() {}

    public TheoryExamDTO(TheoryExam exam) {
        this.id = exam.getId();
        this.examDate = exam.getExamDate();
        this.registrationDate = exam.getRegistrationDate();
        this.status = exam.getStatus().toString();
        this.totalCandidates = exam.getCandidates().size();

        if (exam.getRegisteredBy() != null) {
            this.registeredByName = exam.getRegisteredBy().getName() + " " +
                    exam.getRegisteredBy().getLastname();
        }

        if (exam.getCandidates() != null) {
            this.candidates = exam.getCandidates().stream()
                    .map(c -> new CandidateSimpleDTO(c))
                    .collect(Collectors.toList());
        }
    }
}
