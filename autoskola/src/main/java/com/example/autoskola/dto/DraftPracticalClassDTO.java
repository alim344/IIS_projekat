package com.example.autoskola.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class DraftPracticalClassDTO {


    private String email;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public DraftPracticalClassDTO(){}
    public DraftPracticalClassDTO(String email, LocalDateTime startTime,LocalDateTime endTime){
        this.email = email;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
