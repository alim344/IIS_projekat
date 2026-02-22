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
    private Instructor instructor;
    private Category category;

    public CandidateProfileDTO() {
    }

    public CandidateProfileDTO(String firstName, String lastName, String email, String username, Instructor instructor, TrainingStatus status, Category category) {
        this.name = firstName;
        this.lastname = lastName;
        this.email = email;
        this.username = username;
        this.instructor = instructor;
        this.status = status;
        this.category = category;
    }

    public CandidateProfileDTO(Candidate candidate) {
        this.name = candidate.getName();
        this.lastname = candidate.getLastname();
        this.email = candidate.getEmail();
        this.username = candidate.getEmail();
        this.instructor = candidate.getInstructor();
        this.status = candidate.getStatus();
        this.category = candidate.getCategory();
    }

}
