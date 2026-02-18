package com.example.autoskola.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class InstructorAssignmentDTO {
    private List<InstructorWorkloadDTO> instructors;
    private List<CandidateAssignmentDTO> assignments;
    private String algorithm;

}

