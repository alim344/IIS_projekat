package com.example.autoskola.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProfExamDTO {


    private boolean practical;
    private LocalDateTime date;

}
