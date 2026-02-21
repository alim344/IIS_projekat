package com.example.autoskola.dto;

import com.example.autoskola.model.Candidate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CandidateSimpleDTO {
    private Long id;
    private String name;
    private String lastname;
    private String email;
    private String category;

    public CandidateSimpleDTO() {}

    public CandidateSimpleDTO(Candidate cand) {
        this.id = cand.getId();
        this.name = cand.getName();
        this.lastname = cand.getLastname();
        this.email = cand.getEmail();
        this.category = cand.getCategory() != null ? cand.getCategory().toString() : null;
    }
}