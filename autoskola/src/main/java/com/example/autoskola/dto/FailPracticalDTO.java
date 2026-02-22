package com.example.autoskola.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FailPracticalDTO {

    private String candidate_name;
    private String candidate_email;
    private String candidate_lastName;


    private int totalClasses;
    private double totalHours;

    private double avgClassAWeek;
    private double avgHourAWeek;





}
