package com.example.autoskola.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChangePClassRequestDTO {

    private String text;
    private String instructorEmail;
    private LocalDateTime date;

    public ChangePClassRequestDTO() {}

    public ChangePClassRequestDTO(String text, String instructorEmail,LocalDateTime date) {
        this.text = text;
        this.instructorEmail = instructorEmail;
        this.date = date;
    }

}
