package com.example.autoskola.service;

import com.example.autoskola.dto.FuelRecordDTO;
import com.example.autoskola.dto.VehicleDTO;
import com.example.autoskola.dto.VehicleStatsDTO;
import com.example.autoskola.model.*;


import com.example.autoskola.repository.FuelRecordRepository;
import com.example.autoskola.repository.PracticalClassRepository;
import com.example.autoskola.repository.VehicleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private FuelRecordRepository fuelRecordRepository;

    @Autowired
    private PracticalClassRepository practicalClassRepository;

    public Optional<Vehicle> findById(Long id) {
        return vehicleRepository.findById(id);
    }

    public List<Vehicle> findAll() {
        List<Vehicle> vehicles = vehicleRepository.findAll();

        LocalDate today = LocalDate.now();

        for (Vehicle v : vehicles) {
            if (v.getRegistrationExpiryDate().isBefore(today)) {
                v.setStatus(VehicleStatus.OUT_OF_SERVICE);
            }
        }

        return vehicles;
    }

    public Vehicle addVehicle(VehicleDTO v) {
        if(vehicleRepository.existsByRegistrationNumber(v.getRegistrationNumber())) {
            throw new RuntimeException("Vehicle with this registration already exists.");
        }

        Vehicle vehicle = new Vehicle();
        vehicle.setRegistrationNumber(v.getRegistrationNumber());
        vehicle.setRegistrationExpiryDate(v.getRegistrationExpiryDate());
        vehicle.setStatus(v.getStatus());
        vehicle.setCurrentMileage(v.getCurrentMileage());

        return vehicleRepository.save(vehicle);
    }

    @Transactional
    public Vehicle updateVehicle(Long id, VehicleDTO v) {
        Vehicle vehicle = vehicleRepository.findById(id).orElseThrow(() -> new RuntimeException("Vehicle not found."));

        if (v.getStatus() != null) {
            vehicle.setStatus(v.getStatus());
        }

        if (v.getCurrentMileage() != null) {
            if (v.getCurrentMileage() < vehicle.getCurrentMileage()) {
                throw new IllegalArgumentException("Mileage cannot be lower than current mileage.");
            }
            vehicle.setCurrentMileage(v.getCurrentMileage());
        }

        if (v.getRegistrationExpiryDate() != null) {
            vehicle.setRegistrationExpiryDate(v.getRegistrationExpiryDate());
        }

        if (v.getRegistrationNumber() != null) {
            vehicle.setRegistrationNumber(v.getRegistrationNumber());
        }

        return vehicleRepository.save(vehicle);
    }

    @Transactional
    public Vehicle reportVehicleOutOfService(Long vehicleId) {

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle not found."));


        if (vehicle.getStatus() == VehicleStatus.IN_USE) {
            if (vehicle.getInstructor() != null) {
                vehicle.getInstructor().setVehicle(null);
                vehicle.setInstructor(null);
            }
        }

        vehicle.setStatus(VehicleStatus.OUT_OF_SERVICE);

        return vehicle;
    }

    @Transactional
    public Vehicle returnVehicleToService(Long vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle not found."));

        vehicle.setStatus(VehicleStatus.AVAILABLE);

        return vehicleRepository.save(vehicle);
    }

    public Vehicle getById(long id){
        return vehicleRepository.getById(id);
    }

    public List<Vehicle> findAllByStatus(VehicleStatus status) {
        return vehicleRepository.findAllByStatus(status);
    }

    public Vehicle save(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    public VehicleStatsDTO getVehicleStats(Long vehicleId, int year, int month) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        LocalDate startOfMonth = LocalDate.of(year, month, 1);
        LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());

        List<FuelRecord> fuelRecords = fuelRecordRepository
                .findByVehicleAndRefuelDateBetweenOrderByRefuelDateAsc(vehicle, startOfMonth, endOfMonth);

        VehicleStatsDTO stats = new VehicleStatsDTO();
        stats.setVehicleId(vehicleId);
        stats.setMonth(YearMonth.of(year, month));
        stats.setFuelRecordCount(fuelRecords.size());

        if (!fuelRecords.isEmpty()) {
            double totalLiters = fuelRecords.stream().mapToDouble(FuelRecord::getLiters).sum();
            double totalCost = fuelRecords.stream().mapToDouble(FuelRecord::getTotalCost).sum();
            stats.setTotalLiters(totalLiters);
            stats.setTotalCost(totalCost);
            stats.setAvgCostPerLiter(totalCost / totalLiters);
            stats.setStartingMileage(fuelRecords.get(0).getMileageAtRefuel());
            stats.setEndingMileage(fuelRecords.get(fuelRecords.size() - 1).getMileageAtRefuel());
            stats.setDistanceTraveled(stats.getEndingMileage().intValue() - stats.getStartingMileage().intValue());

            if (stats.getDistanceTraveled() > 0) {
                stats.setAvgConsumption(totalLiters / stats.getDistanceTraveled() * 100);
            }

            stats.setFuelRecords(fuelRecords.stream()
                    .map(this::mapToFuelRecordDTO)
                    .collect(Collectors.toList()));

            Map<Long, Double> fuelByInstructor = new HashMap<>();
            Map<Long, Integer> refuelsByInstructor = new HashMap<>();
            Map<Long, String> instructorNames = new HashMap<>();

            for (FuelRecord record : fuelRecords) {
                Long instructorId = record.getInstructor().getId();
                String instructorName = record.getInstructor().getName() + " " + record.getInstructor().getLastname();

                fuelByInstructor.merge(instructorId, record.getLiters(), Double::sum);
                refuelsByInstructor.merge(instructorId, 1, Integer::sum);
                instructorNames.put(instructorId, instructorName);
            }

            stats.setFuelByInstructor(fuelByInstructor);
            stats.setRefuelsByInstructor(refuelsByInstructor);

            Optional<Map.Entry<Long, Integer>> mostFrequent = refuelsByInstructor.entrySet()
                    .stream()
                    .max(Map.Entry.comparingByValue());

            if (mostFrequent.isPresent()) {
                Long instructorId = mostFrequent.get().getKey();
                stats.setMostFrequentInstructorId(instructorId);
                stats.setMostFrequentInstructorName(instructorNames.get(instructorId));
                stats.setMostFrequentInstructorCount(mostFrequent.get().getValue());
            }

        } else {
            // Prazne vrednosti
            stats.setTotalLiters(0);
            stats.setTotalCost(0);
            stats.setAvgCostPerLiter(0);
            stats.setDistanceTraveled(0);
            stats.setAvgConsumption(0);
            stats.setStartingMileage(null);
            stats.setEndingMileage(null);
            stats.setFuelByInstructor(new HashMap<>());
            stats.setRefuelsByInstructor(new HashMap<>());
            stats.setFuelRecords(new ArrayList<>());
        }

        return stats;
    }

    private FuelRecordDTO mapToFuelRecordDTO(FuelRecord record) {
        FuelRecordDTO dto = new FuelRecordDTO();
        dto.setRefuelDate(record.getRefuelDate());
        dto.setLiters(record.getLiters());
        dto.setTotalCost(record.getTotalCost());
        dto.setMileageAtRefuel(record.getMileageAtRefuel());
        dto.setVehicleId(record.getVehicle().getId());
        dto.setInstructorId(record.getInstructor().getId());
        dto.setInstructorName(record.getInstructor().getName());
        dto.setInstructorLastname(record.getInstructor().getLastname());
        return dto;
    }
}
