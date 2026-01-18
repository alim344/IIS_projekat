package com.example.autoskola.dto;

import com.example.autoskola.model.Category;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationDTO {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String username;
    private Category category;

    public RegistrationDTO(String firstName, String lastName, String email, String password, String username, Category category) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.username = username;
        this.category = category;
    }

    public RegistrationDTO() {}



}
