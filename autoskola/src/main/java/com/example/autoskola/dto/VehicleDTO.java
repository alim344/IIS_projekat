package com.example.autoskola.dto;

import com.example.autoskola.model.Vehicle;
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


    public VehicleDTO(Vehicle vehicle) {
        this.registrationNumber = vehicle.getRegistrationNumber();
        this.registrationExpiryDate = vehicle.getRegistrationExpiryDate();
        this.status = vehicle.getStatus();
        this.currentMileage = vehicle.getCurrentMileage();
    }
}
