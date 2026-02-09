package com.example.autoskola.dto;

import com.example.autoskola.model.Candidate;
import com.example.autoskola.model.Category;
import com.example.autoskola.model.Instructor;
import com.example.autoskola.model.TrainingStatus;

public class CandidateProfileDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private TrainingStatus status;
    private Instructor instructor;
    private Category category;

    public CandidateProfileDTO() {
    }

    public CandidateProfileDTO(String firstName, String lastName, String email, String username, Instructor instructor, TrainingStatus status, Category category) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.instructor = instructor;
        this.status = status;
        this.category = category;
    }

    public CandidateProfileDTO(Candidate candidate) {
        this.firstName = candidate.getName();
        this.lastName = candidate.getLastname();
        this.email = candidate.getEmail();
        this.username = candidate.getEmail();
        this.instructor = candidate.getInstructor();
        this.status = candidate.getStatus();
        this.category = candidate.getCategory();
    }

}
