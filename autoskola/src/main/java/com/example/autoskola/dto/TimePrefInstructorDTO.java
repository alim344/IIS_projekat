package com.example.autoskola.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class TimePrefInstructorDTO {

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    private String candidate_name;
    private String canddiate_lastname;
    private String email;

    public TimePrefInstructorDTO() {}
    public TimePrefInstructorDTO(String candidate_name, String canddiate_lastname, String email, LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.candidate_name = candidate_name;
        this.canddiate_lastname = canddiate_lastname;
        this.email = email;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;

    }



}
