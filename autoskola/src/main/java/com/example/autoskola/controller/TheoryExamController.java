package com.example.autoskola.controller;

import com.example.autoskola.dto.*;
import com.example.autoskola.model.Professor;
import com.example.autoskola.service.ProfessorService;
import com.example.autoskola.service.TheoryExamService;
import com.example.autoskola.util.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/professor/theory-exam")
public class TheoryExamController {

    @Autowired
    private TheoryExamService theoryExamService;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private ProfessorService professorService;


    @GetMapping("/check-eligibility")
    @PreAuthorize("hasAnyRole('ROLE_PROFESSOR', 'ROLE_ADMIN')")
    public ResponseEntity<Map<String, Object>> checkEligibility() {
        boolean hasMinimum = theoryExamService.hasMinimumCandidates();
        List<EligibleCandidateTheoryDTO> candidates = theoryExamService.getEligibleCandidates();

        Map<String, Object> response = new HashMap<>();
        response.put("hasMinimum", hasMinimum);
        response.put("totalEligible", candidates.size());
        response.put("minimumRequired", 20);
        response.put("canRegister", hasMinimum);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/eligible-candidates")
    @PreAuthorize("hasAnyRole('ROLE_PROFESSOR', 'ROLE_ADMIN')")
    public ResponseEntity<List<EligibleCandidateTheoryDTO>> getEligibleCandidates() {
        List<EligibleCandidateTheoryDTO> candidates = theoryExamService.getEligibleCandidates();
        return ResponseEntity.ok(candidates);
    }

    @PostMapping("/register")
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    public ResponseEntity<Map<String, Object>> registerForExam(
            @RequestBody RegisterExamDTO dto,
            HttpServletRequest request) {

        Long professorId = extractProfessorId(request);
        TheoryExamDTO exam = theoryExamService.registerForExam(professorId, dto);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Candidates successfully registered for exam");
        response.put("exam", exam);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{examId}/set-date")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Map<String, Object>> setExamDate(
            @PathVariable Long examId,
            @RequestBody SetTheoryExamDateDTO dto) {

        TheoryExamDTO exam = theoryExamService.setExamDate(examId, dto.getExamDate());

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Exam date set. Candidates have been notified by email.");
        response.put("exam", exam);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-exams")
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    public ResponseEntity<List<TheoryExamDTO>> getMyExams(HttpServletRequest request) {
        Long professorId = extractProfessorId(request);
        return ResponseEntity.ok(theoryExamService.getProfessorExams(professorId));
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ROLE_PROFESSOR', 'ROLE_ADMIN')")
    public ResponseEntity<List<TheoryExamDTO>> getAllExams() {
        return ResponseEntity.ok(theoryExamService.getAllExams());
    }

    @GetMapping("/{examId}")
    @PreAuthorize("hasAnyRole('ROLE_PROFESSOR', 'ROLE_ADMIN')")
    public ResponseEntity<TheoryExamDTO> getExamDetails(@PathVariable Long examId) {
        TheoryExamDTO exam = theoryExamService.getExamDetails(examId);
        return ResponseEntity.ok(exam);
    }

    @DeleteMapping("/{examId}")
    @PreAuthorize("hasAnyRole('ROLE_PROFESSOR', 'ROLE_ADMIN')")
    public ResponseEntity<Map<String, String>> cancelExam(
            @PathVariable Long examId,
            HttpServletRequest request) {

        Long professorId = extractProfessorId(request);

        theoryExamService.cancelExam(examId, professorId);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Exam registration cancelled successfully");

        return ResponseEntity.ok(response);
    }

    private Long extractProfessorId(HttpServletRequest request) {
        String token = tokenUtils.getToken(request);
        String email = tokenUtils.getEmailFromToken(token);
        Professor professor = professorService.findByEmail(email);
        return professor.getId();
    }
}