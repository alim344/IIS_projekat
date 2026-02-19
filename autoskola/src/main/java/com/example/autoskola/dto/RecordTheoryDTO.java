package com.example.autoskola.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RecordTheoryDTO {
    private List<Long> presentCandidatesIds;

    public RecordTheoryDTO() {}

}
