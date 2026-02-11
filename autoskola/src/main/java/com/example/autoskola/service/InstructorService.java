package com.example.autoskola.service;

import com.example.autoskola.dto.InstructorDTO;
import com.example.autoskola.dto.InstructorDocumentsDTO;
import com.example.autoskola.dto.InstructorUpdateDTO;
import com.example.autoskola.model.Instructor;
import com.example.autoskola.model.InstructorDocuments;
import com.example.autoskola.model.Vehicle;
import com.example.autoskola.model.VehicleStatus;

import com.example.autoskola.dto.InstructorRegistrationDTO;

import com.example.autoskola.repository.InstructorRepository;
import com.example.autoskola.repository.VehicleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;


@Service
public class InstructorService {

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private RoleService roleService;

    @Autowired
    private VehicleService vehicleService;

    public Optional<Instructor> findById(Long id) {
        return instructorRepository.findById(id);
    }

    public List<Instructor> findAll() { return instructorRepository.findAll(); }

    @Transactional
    public ResponseEntity<?> assignVehicleToInstructor(Long instructorId, Long vehicleId) {
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(()-> new RuntimeException("Instructor not found."));

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle not found."));

        if(vehicle.getStatus() != VehicleStatus.AVAILABLE) {
            throw new IllegalStateException("Vehicle is not available.");
        }

        if(instructor.getVehicle() != null) {
            Vehicle currentVehicle = instructor.getVehicle();
            currentVehicle.setStatus(VehicleStatus.AVAILABLE);

            instructor.setVehicle(null);
        }

        vehicle.setStatus(VehicleStatus.IN_USE);
        instructor.setVehicle(vehicle);

        return ResponseEntity.ok().build();
    }

    public Instructor save(InstructorRegistrationDTO dto){

        Instructor i = new Instructor();

        i.setName(dto.getFirstName());
        i.setEmail(dto.getEmail());
        i.setLastname(dto.getLastName());
        i.setEnabled(true);
        i.setLastPasswordResetDate(Timestamp.valueOf(LocalDateTime.now()));
        i.setUsername(dto.getUsername());
        i.setPassword(passwordEncoder.encode(dto.getPassword()));
        i.setRole(roleService.findByName("ROLE_INSTRUCTOR"));
        i.setVehicle(vehicleService.getById(dto.getVehicleId()));
        return instructorRepository.save(i);

    }

    public long getIdByEmail(String email){

        Instructor instructor = instructorRepository.findByEmail(email);
        return instructor.getId();

    }

    @Transactional
    public Instructor update(Long instructorId, InstructorUpdateDTO dto) {
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new RuntimeException("Instructor not found."));

        if (dto.getName() != null) instructor.setName(dto.getName());
        if (dto.getLastname() != null) instructor.setLastname(dto.getLastname());

        if (dto.getVehicleId() != null) {
            Vehicle vehicle = vehicleRepository.findById(dto.getVehicleId())
                    .orElseThrow(() -> new RuntimeException("Vehicle not found."));

            if (instructor.getVehicle() != null) {
                instructor.getVehicle().setInstructor(null);
                Vehicle currentVehicle = instructor.getVehicle();
                currentVehicle.setStatus(VehicleStatus.AVAILABLE);
                currentVehicle.setInstructor(null);
            }

            if (vehicle.getInstructor() != null &&
                    !vehicle.getInstructor().getId().equals(instructorId)) {
                throw new RuntimeException("Vehicle already assigned to another instructor.");
            }

            instructor.setVehicle(vehicle);
            vehicle.setStatus(VehicleStatus.IN_USE);
            vehicle.setInstructor(instructor);
        }

        if (dto.getDocuments() != null && !dto.getDocuments().isEmpty()) {
            for (InstructorDocumentsDTO docDto : dto.getDocuments()) {
                instructor.getDocuments().stream()
                        .filter(d -> d.getDocumentType().equals(docDto.getDocumentType()))
                        .findFirst()
                        .ifPresent(d -> d.setExpiryDate(docDto.getExpiryDate()));
            }
        }

        return instructorRepository.save(instructor);
    }

    @Transactional
    public void removeVehicle(Long id) {
        Instructor instructor = instructorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Instructor not found."));

        Vehicle vehicle = vehicleRepository.findById(instructor.getVehicle().getId())
                .orElseThrow(()->new RuntimeException("Vehicle not found"));

        vehicle.setInstructor(null);
        vehicle.setStatus(VehicleStatus.AVAILABLE);
        instructor.setVehicle(null);

    }

}
