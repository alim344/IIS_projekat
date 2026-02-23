package com.example.autoskola.service;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
class InstructorAssignmentInfo {
    private Long instructorId;
    private String name;
    private String lastName;
    private int currentCount;
    private int maxCapacity;
    private int freeSpots;
    private double priority;

    public InstructorAssignmentInfo() {}
    public InstructorAssignmentInfo(Long instructorId, String name, String lastName,
                                    int currentCount, int maxCapacity, int freeSpots, double priority) {
        this.instructorId = instructorId;
        this.name = name;
        this.lastName = lastName;
        this.currentCount = currentCount;
        this.maxCapacity = maxCapacity;
        this.freeSpots = freeSpots;
        this.priority = priority;
    }

}
