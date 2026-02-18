package com.example.autoskola.dto;

import com.example.autoskola.model.Candidate;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CandidatePracticalDTO {

    private long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String instructorName;
    private String instructorLastName;
    private String instructorEmail;
    private String preferredLocation;
    private boolean accepted;
    private String note;


    public CandidatePracticalDTO() {}
    public CandidatePracticalDTO(long id, LocalDateTime startTime, LocalDateTime endTime, String instructorName, String instructorLastName, String preferredLocation, boolean accepted, String note) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.instructorName = instructorName;
        this.instructorLastName = instructorLastName;
        this.preferredLocation = preferredLocation;
        this.accepted = accepted;
        this.note = note;
    }


}
