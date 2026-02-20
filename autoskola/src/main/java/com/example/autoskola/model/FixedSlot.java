package com.example.autoskola.model;

import java.time.LocalTime;

public enum FixedSlot {
    MORNING(LocalTime.of(8, 0), LocalTime.of(9, 0)),
    AFTERNOON(LocalTime.of(14, 0), LocalTime.of(15, 0)),
    EVENING(LocalTime.of(18, 0), LocalTime.of(19, 0));

    private final LocalTime startTime;
    private final LocalTime endTime;

    FixedSlot(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }
}
