package com.example.autoskola.service;

import com.example.autoskola.dto.ProfessorRegistrationDTO;
import com.example.autoskola.model.Professor;
import com.example.autoskola.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
public class ProfessorService {

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private RoleService roleService;


    public Professor saveFromDTO(ProfessorRegistrationDTO dto) {

        Professor p = new Professor();
        p.setName(dto.getFirstName());
        p.setEmail(dto.getEmail());
        p.setLastname(dto.getLastName());
        p.setEnabled(true);
        p.setLastPasswordResetDate(Timestamp.valueOf(LocalDateTime.now()));
        p.setUsername(dto.getUsername());
        p.setPassword(passwordEncoder.encode(dto.getPassword()));
        p.setRole(roleService.findByName("ROLE_PROFESSOR"));
        return professorRepository.save(p);
    }


}
