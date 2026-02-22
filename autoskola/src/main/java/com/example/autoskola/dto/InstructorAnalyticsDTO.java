package com.example.autoskola.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InstructorAnalyticsDTO {

    private int numberOfExams;
    private int numberOfPassedExams;
    private int numberOfFailedExams;
    private double passPercentage;
    private double failedPercentage;
    private List<FailPracticalDTO> failed;

}
