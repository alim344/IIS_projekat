package com.example.autoskola.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TimeRangeDTO {

    LocalDateTime start;
    LocalDateTime end;

    public TimeRangeDTO(LocalDateTime start, LocalDateTime end) {
        this.start = start;
        this.end = end;
    }
}
