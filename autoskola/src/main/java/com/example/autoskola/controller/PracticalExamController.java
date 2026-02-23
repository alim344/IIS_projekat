package com.example.autoskola.controller;

import com.example.autoskola.dto.ExamSuggestionDTO;
import com.example.autoskola.dto.PracticalExamScheduleRequestDTO;
import com.example.autoskola.dto.ProfessorAvailabilityDTO;
import com.example.autoskola.model.PracticalExam;
import com.example.autoskola.model.Professor;
import com.example.autoskola.service.PracticalExamSchedulingService;
import com.example.autoskola.service.ProfessorSchedulingService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/practical-exam")
@RequiredArgsConstructor
public class PracticalExamController {
    private final PracticalExamSchedulingService examSchedulingService;
    private final ProfessorSchedulingService professorSchedulingService;

    @GetMapping("/suggest-professor")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> suggestProfessor(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime) {
        try {
            Professor professor = examSchedulingService.suggestProfessorForExam(dateTime);
            return ResponseEntity.ok(professor);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/professors-availability")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<ProfessorAvailabilityDTO>> getProfessorsAvailability(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime) {
        return ResponseEntity.ok(professorSchedulingService.calculateProfessorsAvailability(dateTime));
    }

    @PostMapping("/schedule")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> scheduleExam(@RequestBody PracticalExamScheduleRequestDTO request) {
        try {
            PracticalExam exam = examSchedulingService.scheduleExam(request);
            return ResponseEntity.ok(exam);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{examId}/change-witness")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> changeWitness(
            @PathVariable Long examId,
            @RequestParam Long newProfessorId) {
        try {
            PracticalExam exam = examSchedulingService.changeWitness(examId, newProfessorId);
            return ResponseEntity.ok(exam);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
