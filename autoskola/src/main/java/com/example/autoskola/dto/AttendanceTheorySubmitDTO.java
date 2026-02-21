package com.example.autoskola.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AttendanceTheorySubmitDTO {
    private Long classId;
    private List<Long> presentCandidateIds;

    public AttendanceTheorySubmitDTO() {}
}
