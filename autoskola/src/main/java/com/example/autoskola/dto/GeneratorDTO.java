package com.example.autoskola.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class GeneratorDTO {

    private List<LocalDate> lightDays;

    private List<String> emails;

    public GeneratorDTO() {
    }
}
