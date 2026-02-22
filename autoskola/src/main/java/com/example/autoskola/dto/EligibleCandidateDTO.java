package com.example.autoskola.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EligibleCandidateDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String category;
    private Long instructorId;
}