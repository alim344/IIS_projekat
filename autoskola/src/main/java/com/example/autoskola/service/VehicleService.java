package com.example.autoskola.service;

import com.example.autoskola.dto.VehicleDTO;
import com.example.autoskola.model.Instructor;
import com.example.autoskola.model.Vehicle;
import com.example.autoskola.model.VehicleStatus;


import com.example.autoskola.repository.VehicleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

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
            throw new RuntimeException("Vehicle with this registration already exists");
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
        Vehicle vehicle = vehicleRepository.findById(id).orElseThrow(() -> new RuntimeException("Vehicle not found"));

        if (v.getStatus() != null) {
            vehicle.setStatus(v.getStatus());
        }

        if (v.getCurrentMileage() != null) {
            if (v.getCurrentMileage() < vehicle.getCurrentMileage()) {
                throw new IllegalArgumentException("Mileage cannot be lower than current mileage");
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
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));


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
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        vehicle.setStatus(VehicleStatus.AVAILABLE);

        return vehicleRepository.save(vehicle);
    }


    public Vehicle getById(long id){
        return vehicleRepository.getById(id);
    }

}
