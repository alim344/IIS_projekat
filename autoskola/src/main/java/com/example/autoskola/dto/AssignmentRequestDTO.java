package com.example.autoskola.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class AssignmentRequestDTO {
    private List<Long> candidateIds;
    private String algorithm;
    private Map<Long, Long> manualAssignments;
    private boolean confirmAssignment;
}
