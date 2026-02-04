package com.example.autoskola.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfessorRegistrationDTO {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String username;

    public ProfessorRegistrationDTO() {}
    public ProfessorRegistrationDTO(String firstName, String lastName, String email, String password, String username) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.username = username;
    }


}
