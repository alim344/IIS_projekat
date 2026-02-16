package com.example.autoskola.dto;

import com.example.autoskola.model.Candidate;
import com.example.autoskola.model.TrainingStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
@Getter
@Setter
public class PreferencesUpdateDTO {

    private String preferredLocation;

    private LocalDate preferredDate;
    private LocalTime preferredStartTime;
    private LocalTime preferredEndTime;

    public PreferencesUpdateDTO() {}

    public PreferencesUpdateDTO(String preferredLocation, LocalDate preferredDate, LocalTime preferredStartTime, LocalTime preferredEndTime) {
        this.preferredLocation = preferredLocation;
        this.preferredDate = preferredDate;
        this.preferredStartTime = preferredStartTime;
        this.preferredEndTime = preferredEndTime;
    }

}
