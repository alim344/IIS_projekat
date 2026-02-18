package com.example.autoskola.dto;

import com.example.autoskola.model.Candidate;
import com.example.autoskola.model.PracticalClass;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
public class PracticalDTO {

    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String name;
    private String lastname;
    private String email;
    private String category;
    private boolean accepted;
    private String note;
    private String preferredLocation;

    public PracticalDTO() {}
    public PracticalDTO(Long id, String name, String lastname, String category,LocalDateTime startTime, LocalDateTime endTime,String email,boolean accepted,String note,String preferredLocation) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.category = category;
        this.startTime = startTime;
        this.endTime = endTime;
        this.email = email;
        this.accepted = accepted;
        this.note = note;
        this.preferredLocation = preferredLocation;
    }

    public PracticalDTO(Candidate candidate, PracticalClass pc) {
        this.id = candidate.getId();
        this.name = candidate.getName();
        this.lastname = candidate.getLastname();
        this.category = candidate.getCategory().toString();
        this.startTime = pc.getStartTime();
        this.endTime = pc.getEndTime();
        this.email = candidate.getEmail();
        this.accepted = pc.isAccepted();
    }


}
