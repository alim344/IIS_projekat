package com.example.autoskola.controller;

import com.example.autoskola.dto.InstructorDTO;
import com.example.autoskola.dto.InstructorUpdateDTO;
import com.example.autoskola.dto.ProfessorDTO;
import com.example.autoskola.model.Instructor;
import com.example.autoskola.model.Professor;
import com.example.autoskola.model.User;
import com.example.autoskola.service.ProfessorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

        return ResponseEntity.ok(new ProfessorDTO(professor));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<ProfessorDTO>> getAllProfessors() {
        List<Professor> professors = professorService.findAll();

        List<ProfessorDTO> dtos = professors.stream()
                .map(p -> new ProfessorDTO(p))
                .toList();

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ProfessorDTO> getProfessorById(@PathVariable Long id) {
        Professor professor = professorService.findById(id)
                .orElseThrow(() -> new RuntimeException("Professor not found"));

        return ResponseEntity.ok(new ProfessorDTO(professor));
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ProfessorDTO> updateInstructor(@PathVariable Long id, @RequestBody ProfessorDTO professorDto) {
        Professor updateProfessor = professorService.update(id, professorDto);

        ProfessorDTO response = new ProfessorDTO(updateProfessor);

        return ResponseEntity.ok(response);
    }
}
