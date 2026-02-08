package com.example.autoskola.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
public class PracticalDTO {

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String name;
    private String lastname;
    private String email;
    private String category;
    private boolean accepted;

    public PracticalDTO() {}
    public PracticalDTO(String name, String lastname, String category,LocalDateTime startTime, LocalDateTime endTime,String email,boolean accepted) {
        this.name = name;
        this.lastname = lastname;
        this.category = category;
        this.startTime = startTime;
        this.endTime = endTime;
        this.email = email;
        this.accepted = accepted;
    }


}
