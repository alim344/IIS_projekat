package com.example.autoskola.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.YearMonth;

@Getter
@Setter
@NoArgsConstructor
public class VehicleStatsDTO {
    private Long vehicleId;
    private YearMonth month;
    private int distanceTraveled;
    private double totalLiters;
    private double totalCost;
    private double avgConsumption;
    private double avgCostPerLiter;
    private int fuelRecordCount;
    private Integer startingMileage;
    private Integer endingMileage;
}
