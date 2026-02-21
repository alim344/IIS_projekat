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
    private String instructorName;
    private String instructorLastname;

    public FuelRecordDTO() {}

    public FuelRecordDTO(FuelRecord record) {
        this.refuelDate = record.getRefuelDate();
        this.liters = record.getLiters();
        this.totalCost = record.getTotalCost();
        this.mileageAtRefuel = record.getMileageAtRefuel();
        this.vehicleId = record.getVehicle().getId();
        this.instructorId = record.getInstructor().getId();
        this.instructorName = record.getInstructor().getName();
        this.instructorLastname = record.getInstructor().getLastname();
    }

}
