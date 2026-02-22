package com.example.autoskola.dto;

import com.example.autoskola.model.FixedSlot;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
@Getter
@Setter
public class ManualTheoryClassRequestDTO {
    private LocalDate date;
    private FixedSlot slot;
    private Long professorId;
    private Long lessonId;
    private List<Long> candidateIds;
}
