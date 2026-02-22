package com.example.autoskola.dto;

import com.example.autoskola.model.Candidate;
import com.example.autoskola.model.Category;
import com.example.autoskola.model.Instructor;
import com.example.autoskola.model.TrainingStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CandidateProfileDTO {
    private Long candidateId;
    private String name;
    private String lastname;
    private String email;
    private String username;
    private TrainingStatus status;
    private String instructor;
    private Category category;

    public CandidateProfileDTO() {
    }

    public CandidateProfileDTO(Candidate candidate) {
        this.name = candidate.getName();
        this.lastname = candidate.getLastname();
        this.email = candidate.getEmail();
        this.username = candidate.getUsername();
        if (candidate.getInstructor() != null) {
            this.instructor = candidate.getInstructor().getName() + " " + candidate.getInstructor().getLastname();
        } else {
            this.instructor = null;
        }
        this.status = candidate.getStatus();
        this.category = candidate.getCategory();
    }

}
