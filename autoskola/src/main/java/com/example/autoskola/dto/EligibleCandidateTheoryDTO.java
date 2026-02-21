package com.example.autoskola.dto;

import com.example.autoskola.model.Candidate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EligibleCandidateTheoryDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String category;
    private Integer completedLessons;

    public EligibleCandidateTheoryDTO() {}

    public EligibleCandidateTheoryDTO(Candidate candidate) {
        this.id = candidate.getId();
        this.firstName = candidate.getName();
        this.lastName = candidate.getLastname();
        this.email = candidate.getEmail();
        this.category = candidate.getCategory().toString();
        this.completedLessons = candidate.getAttendedLessons().size();
    }
}
