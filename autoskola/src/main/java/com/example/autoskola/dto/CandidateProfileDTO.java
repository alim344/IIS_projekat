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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public TrainingStatus getStatus() {
        return status;
    }

    public void setStatus(TrainingStatus status) {
        this.status = status;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
