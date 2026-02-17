package com.example.autoskola.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePClassRequestDTO {

    private String text;
    private String instructorEmail;

    public ChangePClassRequestDTO() {}

    public ChangePClassRequestDTO(String text, String instructorEmail) {
        this.text = text;
        this.instructorEmail = instructorEmail;
    }

}
