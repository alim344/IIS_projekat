package com.example.autoskola.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfessorAvailabilityDTO {
    private Long professorId;
    private String name;
    private String lastName;
    private boolean available;
    private int currentExams; // koliko ispita ima u toj nedelji
    private int totalExams; // ukupno ispita u nedelji (kapacitet)
    private double workloadPercentage; // procenat opterećenja
    private String reason; // ako nije dostupan, zašto
}