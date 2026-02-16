package com.example.autoskola.dto;

import com.example.autoskola.model.Candidate;
import com.example.autoskola.model.TimePreference;

import java.time.LocalDate;
import java.time.LocalTime;

public class PreferencesDTO {

    private String preferredLocation;
    private LocalDate preferredDate;
    private LocalTime preferredStartTime;
    private LocalTime preferredEndTime;

    public PreferencesDTO(String preferredLocation, LocalDate preferredDate, LocalTime preferredStartTime, LocalTime preferredEndTime) {
        this.preferredLocation = preferredLocation;
        this.preferredDate = preferredDate;
        this.preferredStartTime = preferredStartTime;
        this.preferredEndTime = preferredEndTime;
    }

    public PreferencesDTO(Candidate candidate) {
        this.preferredLocation = candidate.getPreferredLocation();

        if (candidate.getTimePreference() != null) {
            TimePreference tp = candidate.getTimePreference();
            this.preferredDate = tp.getDate();
            this.preferredStartTime = tp.getStartTime();
            this.preferredEndTime = tp.getEndTime();
        }
    }

    public String getPreferredLocation() {
        return preferredLocation;
    }

    public void setPreferredLocation(String preferredLocation) {
        this.preferredLocation = preferredLocation;
    }

    public LocalDate getPreferredDate() {
        return preferredDate;
    }

    public void setPreferredDate(LocalDate preferredDate) {
        this.preferredDate = preferredDate;
    }

    public LocalTime getPreferredStartTime() {
        return preferredStartTime;
    }

    public void setPreferredStartTime(LocalTime preferredStartTime) {
        this.preferredStartTime = preferredStartTime;
    }

    public LocalTime getPreferredEndTime() {
        return preferredEndTime;
    }

    public void setPreferredEndTime(LocalTime preferredEndTime) {
        this.preferredEndTime = preferredEndTime;
    }
}
