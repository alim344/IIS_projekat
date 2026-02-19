package com.example.autoskola.repository;

import com.example.autoskola.model.FuelRecord;
import com.example.autoskola.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface FuelRecordRepository extends JpaRepository<FuelRecord, Long> {
    List<FuelRecord> findFuelRecordByVehicle(Vehicle vehicle);

    List<FuelRecord> findByVehicleAndRefuelDateBetweenOrderByRefuelDateAsc(Vehicle vehicle, LocalDate startOfMonth,
                                                                           LocalDate endOfMonth);
}
