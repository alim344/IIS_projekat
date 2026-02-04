package com.example.autoskola.controller;

import com.example.autoskola.dto.FuelRecordDTO;
import com.example.autoskola.model.Instructor;
import com.example.autoskola.model.FuelRecord;
import com.example.autoskola.model.Vehicle;
import com.example.autoskola.service.FuelRecordService;
import com.example.autoskola.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fuel-records")
@RequiredArgsConstructor
public class FuelRecordController {

    @Autowired
    private FuelRecordService fuelRecordService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_INSTRUCTOR')")
    public ResponseEntity<?> createFuelRecord(@RequestBody FuelRecordDTO dto, Authentication authentication) {
        Instructor instructor = (Instructor) authentication.getPrincipal();

        FuelRecord record = fuelRecordService.createFuelRecord(dto, instructor);

        FuelRecordDTO recordDto = new FuelRecordDTO(record);

        return ResponseEntity.ok(recordDto);
    }

    @GetMapping("/vehicle/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_INSTRUCTOR')")
    public ResponseEntity<List<FuelRecordDTO>> getRecordsByVehicle(@PathVariable Long id) {
        List<FuelRecord> records = fuelRecordService.findByVehicle(id);

        List<FuelRecordDTO> dtos = records.stream()
                .map(FuelRecordDTO::new)
                .toList();

        return ResponseEntity.ok(dtos);
    }
}
