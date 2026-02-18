package com.example.autoskola.dto;

import com.example.autoskola.model.PracticalClass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecordPracticalDTO {
    private String notes;
    private Integer mileage;

    public RecordPracticalDTO() {}
}
