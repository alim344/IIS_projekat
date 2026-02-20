package com.example.autoskola.controller;

import com.example.autoskola.dto.CandidateTheoryClassDTO;
import com.example.autoskola.model.Candidate;
import com.example.autoskola.model.TheoryClass;
import com.example.autoskola.service.CandidateService;
import com.example.autoskola.service.TheoryClassService;
import com.example.autoskola.service.TheorySchedulingService;
import com.example.autoskola.util.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/theoryclass")
public class TheoryClassController {

    private final TokenUtils tokenUtils;
    private final CandidateService candidateService;
    private final TheoryClassService theoryClassService;
    private final TheorySchedulingService theorySchedulingService;

    public TheoryClassController(TokenUtils tokenUtils, CandidateService candidateService, TheoryClassService theoryClassService, TheorySchedulingService theorySchedulingService) {
        this.tokenUtils = tokenUtils;
        this.candidateService = candidateService;
        this.theoryClassService = theoryClassService;
        this.theorySchedulingService = theorySchedulingService;
    }

    @GetMapping("/candidateschedule")
    public ResponseEntity<List<CandidateTheoryClassDTO>> getCandidateFullSchedule(HttpServletRequest request) {
        String token = tokenUtils.getToken(request);
        String email = tokenUtils.getEmailFromToken(token);
        Candidate c = candidateService.findByEmail(email);

        return ResponseEntity.ok(theoryClassService.getFullScheduleDTO(c));
    }


    @PatchMapping("/enroll/{classId}")
    public ResponseEntity<String> enroll(@PathVariable long classId, HttpServletRequest request) {
        try{
            String token = tokenUtils.getToken(request);
            String email = tokenUtils.getEmailFromToken(token);
            Candidate c = candidateService.findByEmail(email);
            theoryClassService.enroll(c, classId);
            return ResponseEntity.ok("Enrolled");
        }catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/auto-generate")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Map<String, Object>> autoGenerateSchedule(
            Authentication authentication) {

        System.out.println("Starting automatic schedule generation...");

        List<TheoryClass> classes = theorySchedulingService.generateSchedule();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("totalClasses", classes.size());
        response.put("message", "Schedule generated successfully for " + classes.size() + " classes");

        long uniqueLessons = classes.stream()
                .map(c -> c.getTheoryLesson().getId())
                .distinct()
                .count();
        response.put("uniqueLessons", uniqueLessons);

        long uniqueCandidates = classes.stream()
                .flatMap(c -> c.getStudents().stream())
                .map(c -> c.getId())
                .distinct()
                .count();
        response.put("candidatesScheduled", uniqueCandidates);

        return ResponseEntity.ok(response);
    }



}
