package com.example.autoskola.service;

import com.example.autoskola.dto.FuelRecordDTO;
import com.example.autoskola.model.FuelRecord;
import com.example.autoskola.model.Instructor;
import com.example.autoskola.model.Vehicle;
import com.example.autoskola.repository.FuelRecordRepository;
import com.example.autoskola.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FuelRecordService {

    @Autowired
    private FuelRecordRepository fuelRecordRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Transactional
    public FuelRecord createFuelRecord(FuelRecordDTO dto, Instructor instructor) {
        Vehicle vehicle = vehicleRepository.findById(dto.getVehicleId())
                .orElseThrow(() -> new RuntimeException("Vehicle not found."));

        if (dto.getMileageAtRefuel() < vehicle.getCurrentMileage()) {
            throw new IllegalArgumentException("Mileage cannot be lower than current vehicle mileage.");
        }

        FuelRecord record = new FuelRecord();
        record.setLiters(dto.getLiters());
        record.setRefuelDate(dto.getRefuelDate());
        record.setTotalCost(dto.getTotalCost());
        record.setMileageAtRefuel(dto.getMileageAtRefuel());
        record.setInstructor(instructor);
        record.setVehicle(vehicle);

        FuelRecord savedRecord = fuelRecordRepository.save(record);

        vehicle.setCurrentMileage(dto.getMileageAtRefuel());
        vehicleRepository.save(vehicle);

        return savedRecord;
    }

    public List<FuelRecord> findByVehicle(Long vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        return fuelRecordRepository.findFuelRecordByVehicle(vehicle);
    }
}
