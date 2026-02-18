package com.example.autoskola.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InstructorWorkloadDTO {
    private Long instructorId;
    private String name;
    private String lastName;
    private int currentStudentCount;
    private int maxCapacity;
    private double workloadPercentage;
}

