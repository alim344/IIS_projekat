package com.example.autoskola.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfessorDTO {
    private String username;

    private String email;

    private String name;

    private String lastName;

    public ProfessorDTO(String username, String email, String name, String lastName) {
        this.username = username;
        this.email = email;
        this.name = name;
        this.lastName = lastName;
    }
}
