package com.example.autoskola.service;

import com.example.autoskola.dto.InstructorRegistrationDTO;
import com.example.autoskola.model.Instructor;
import com.example.autoskola.repository.InstructorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
public class InstructorService {

    @Autowired
    private InstructorRepository instructorRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private RoleService roleService;
    @Autowired
    private VehicleService vehicleService;


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




}
