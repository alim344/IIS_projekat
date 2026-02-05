package com.example.autoskola.controller;

import com.example.autoskola.dto.ProfessorDTO;
import com.example.autoskola.model.Professor;
import com.example.autoskola.model.User;
import com.example.autoskola.service.ProfessorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/professors")
@RequiredArgsConstructor
public class ProfessorController {
    @Autowired
    private ProfessorService professorService;

    @GetMapping("/me")
    public ResponseEntity<ProfessorDTO> getMyProfile(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        Professor professor = professorService.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Professor not found"));

        return ResponseEntity.ok(new ProfessorDTO(professor.getUsername(), professor.getEmail(),
                professor.getName(), professor.getLastname()));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<ProfessorDTO>> getAllProfessors() {
        List<Professor> professors = professorService.findAll();

        List<ProfessorDTO> dtos = professors.stream()
                .map(p -> new ProfessorDTO(p.getUsername(), p.getEmail(), p.getName(),
                        p.getLastname()))
                .toList();

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ProfessorDTO> getProfessorById(@PathVariable Long id) {
        Professor professor = professorService.findById(id)
                .orElseThrow(() -> new RuntimeException("Professor not found"));

        return ResponseEntity.ok(new ProfessorDTO(professor.getUsername(), professor.getEmail(),
                professor.getName(), professor.getLastname()));
    }
}
