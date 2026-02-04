package com.example.autoskola.dto;

import com.example.autoskola.model.FuelRecord;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class FuelRecordDTO {
    private LocalDate refuelDate;
    private Double liters;
    private Double totalCost;
    private Integer mileageAtRefuel;
    private Long vehicleId;
    private Long instructorId;

    public FuelRecordDTO() {}

    public FuelRecordDTO(FuelRecord record) {
        this.refuelDate = record.getRefuelDate();
        this.liters = record.getLiters();
        this.totalCost = record.getTotalCost();
        this.mileageAtRefuel = record.getMileageAtRefuel();
        this.vehicleId = record.getVehicle().getId();
        this.instructorId = record.getInstructor().getId();
    }

    public FuelRecordDTO(LocalDate refuelDate, Double liters, Double totalCost, Integer mileageAtRefuel, Long vehicleId, Long instructorId) {
        this.refuelDate = refuelDate;
        this.liters = liters;
        this.totalCost = totalCost;
        this.mileageAtRefuel = mileageAtRefuel;
        this.vehicleId = vehicleId;
        this.instructorId = instructorId;
    }
}
