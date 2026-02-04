package com.example.autoskola.dto;

import com.example.autoskola.model.VehicleStatus;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDate;

@Getter
@Setter
public class VehicleDTO {

    private String registrationNumber;

    private LocalDate registrationExpiryDate;

    private VehicleStatus status;

    private Integer currentMileage;

    private Long instructorId;

    public VehicleDTO(String registrationNumber, LocalDate registrationExpiryDate,
                      VehicleStatus status, Integer currentMileage, Long instructorId) {
        this.registrationNumber = registrationNumber;
        this.registrationExpiryDate = registrationExpiryDate;
        this.status = status;
        this.currentMileage = currentMileage;
        this.instructorId = instructorId;
    }
}
