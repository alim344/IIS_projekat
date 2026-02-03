package com.example.autoskola.dto;

import com.example.autoskola.model.Instructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InstructorDTO {

    private String username;

    private String email;

    private String name;

    private String lastName;

    private Long vehicle_id;

    public InstructorDTO(String username, String email, String name, String lastName) {
            this.username = username;
            this.email = email;
            this.name = name;
            this.lastName = lastName;
        }

}
