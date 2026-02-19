package com.example.autoskola.controller;

import com.example.autoskola.dto.InstructorDTO;
import com.example.autoskola.dto.VehicleDTO;
import com.example.autoskola.dto.VehicleStatsDTO;
import com.example.autoskola.model.Vehicle;
import com.example.autoskola.model.VehicleStatus;
import com.example.autoskola.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<VehicleDTO>> getAll() {
        List<Vehicle> vehicles = vehicleService.findAll();

        List<VehicleDTO> dtos = vehicles.stream()
                .map(v -> new VehicleDTO(v))
                .toList();

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<VehicleDTO> findById(@PathVariable Long id) {
        Vehicle vehicle = vehicleService.findById(id)
                .orElseThrow(()-> new RuntimeException("Vehicle does not exist."));
        return ResponseEntity.ok(new VehicleDTO(vehicle));
    }

    @GetMapping("/available")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<VehicleDTO>> getAvailableVehicles() {
        List<Vehicle> vehicles = vehicleService.findAllByStatus(VehicleStatus.AVAILABLE);

        List<VehicleDTO> dtos = vehicles.stream()
                .map(v -> new VehicleDTO(v))
                .toList();

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}/stats")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_INSTRUCTOR')")
    public ResponseEntity<VehicleStatsDTO> getVehicleStats(
            @PathVariable Long id,
            @RequestParam int year,
            @RequestParam int month) {

        VehicleStatsDTO stats = vehicleService.getVehicleStats(id, year, month);
        return ResponseEntity.ok(stats);
    }

}
