package com.example.autoskola.dto;

import com.example.autoskola.model.Vehicle;
import com.example.autoskola.model.VehicleStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class VehicleDTO {
    private Long id;

    private String registrationNumber;

    private LocalDate registrationExpiryDate;

    private VehicleStatus status;

    private Integer currentMileage;


    public VehicleDTO(Vehicle vehicle) {
        this.id = vehicle.getId();
        this.registrationNumber = vehicle.getRegistrationNumber();
        this.registrationExpiryDate = vehicle.getRegistrationExpiryDate();
        this.status = vehicle.getStatus();
        this.currentMileage = vehicle.getCurrentMileage();
    }
}
