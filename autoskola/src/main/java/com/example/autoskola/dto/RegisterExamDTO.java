package com.example.autoskola.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
@Getter
@Setter
public class RegisterExamDTO {
        private LocalDate examDate;
        private List<Long> candidateIds;

        public RegisterExamDTO() {}
}
