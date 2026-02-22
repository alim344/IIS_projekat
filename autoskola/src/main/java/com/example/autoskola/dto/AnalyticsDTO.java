package com.example.autoskola.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class AnalyticsDTO {
    private Map<String, Integer> candidatePreferences = new HashMap<>();

    private Map<String, Integer> theoryClassesBySlot = new HashMap<>();
    private Double averageTheoryClassOccupancy;
    private Map<String, Double> occupancyBySlot = new HashMap<>();

    private Map<String, Integer> practicalClassesBySlot = new HashMap<>();
    private Map<String, Integer> practicalClassesByInstructor = new HashMap<>();
    private Map<String, Integer> completedPracticalByInstructor = new HashMap<>();

    private Map<String, Integer> candidatesByCategory = new HashMap<>();
    private Map<String, Integer> candidatesByStatus = new HashMap<>();
    private Double averageLessonsCompleted;

    private Map<String, Double> attendanceRateByLesson = new HashMap<>();
    private Integer totalTheoryClasses;
    private Integer totalPracticalClasses;
    private Integer totalCandidates;
    private Integer candidatesReadyForExam;

    public AnalyticsDTO() {}
}
