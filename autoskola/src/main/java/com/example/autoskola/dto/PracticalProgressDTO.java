package com.example.autoskola.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PracticalProgressDTO {
    private int attended;
    private int total = 40;
    private double percentage;
}
