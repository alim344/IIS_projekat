package com.example.autoskola.controller;

import com.example.autoskola.dto.VehicleDTO;
import com.example.autoskola.model.Vehicle;
import com.example.autoskola.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> addVehicle(@RequestBody VehicleDTO vehicleDTO) {
        Vehicle vehicle = vehicleService.addVehicle(vehicleDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(vehicle);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateVehicle(@PathVariable Long id, @RequestBody VehicleDTO vehicleDTO) {
        vehicleService.updateVehicle(id, vehicleDTO);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/out-of-service")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_INSTRUCTOR')")
    public ResponseEntity<Vehicle> reportOutOfService(@PathVariable Long id) {
        Vehicle vehicle = vehicleService.reportVehicleOutOfService(id);
        return ResponseEntity.ok(vehicle);
    }

    @PutMapping("/{id}/return-to-service")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Vehicle> returnToService(@PathVariable Long id) {
        Vehicle vehicle = vehicleService.returnVehicleToService(id);
        return ResponseEntity.ok(vehicle);
    }
}
