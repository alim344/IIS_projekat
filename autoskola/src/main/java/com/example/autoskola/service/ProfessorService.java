package com.example.autoskola.service;

import com.example.autoskola.dto.ProfessorDTO;
import com.example.autoskola.dto.ProfessorRegistrationDTO;
import com.example.autoskola.model.Professor;
import com.example.autoskola.repository.ProfessorRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    public Optional<Professor> findById(Long id) {
        return professorRepository.findById(id);
    }

    public List<Professor> findAll() {
        return professorRepository.findAll();
    }

    @Transactional
    public Professor update(Long professorId, ProfessorDTO dto) {
        Professor professor = professorRepository.findById(professorId)
                .orElseThrow(() -> new RuntimeException("Professor not found."));

        if (dto.getName() != null) professor.setName(dto.getName());
        if (dto.getLastName() != null) professor.setLastname(dto.getLastName());


        return professorRepository.save(professor);
    }

}
