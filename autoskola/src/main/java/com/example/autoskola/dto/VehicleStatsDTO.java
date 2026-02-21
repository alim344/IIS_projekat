package com.example.autoskola.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.YearMonth;
import java.util.Map;
import java.util.List;

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

    private Long mostFrequentInstructorId;
    private String mostFrequentInstructorName;
    private int mostFrequentInstructorCount;
    private Map<Long, Double> fuelByInstructor;
    private Map<Long, Integer> refuelsByInstructor;

    private Integer practicalClassesCount;
    private Double fuelPerClass;
    private Double costPerClass;

    private List<FuelRecordDTO> fuelRecords;
}
