package com.example.autoskola.dto;

import com.example.autoskola.model.Professor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProfessorDTO {
    private Long id;

    private String username;

    private String email;

    private String name;

    private String lastName;

    public ProfessorDTO(Professor professor) {
        this.id = professor.getId();
        this.username = professor.getUsername();
        this.email = professor.getEmail();
        this.name = professor.getName();
        this.lastName = professor.getLastname();
    }
    
}
