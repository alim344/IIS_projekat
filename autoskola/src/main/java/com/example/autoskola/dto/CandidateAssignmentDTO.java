package com.example.autoskola.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CandidateAssignmentDTO {
    private Long candidateId;
    private String firstName;
    private String lastName;
    private Long assignedInstructorId;
    private String assignedInstructorName;
    private String status;
    private boolean manuallyAssigned;
}

