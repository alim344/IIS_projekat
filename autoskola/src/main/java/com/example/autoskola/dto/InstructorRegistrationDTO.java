package com.example.autoskola.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InstructorRegistrationDTO {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String username;
    private Long vehicleId;

    public InstructorRegistrationDTO(String firstName, String lastName, String email, String password, String username,  Long vehicleId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.username = username;
        this.vehicleId = vehicleId;
    }

    public InstructorRegistrationDTO() {}
}
