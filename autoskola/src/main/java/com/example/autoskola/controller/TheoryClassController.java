package com.example.autoskola.controller;

import com.example.autoskola.dto.CandidateTheoryClassDTO;
import com.example.autoskola.dto.TheoryClassInfoDTO;
import com.example.autoskola.model.Candidate;
import com.example.autoskola.model.Professor;
import com.example.autoskola.service.CandidateService;
import com.example.autoskola.service.ProfessorService;
import com.example.autoskola.service.TheoryClassService;
import com.example.autoskola.util.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/theoryclass")
public class TheoryClassController {

    private final TokenUtils tokenUtils;
    private final CandidateService candidateService;
    private final TheoryClassService theoryClassService;
    private final ProfessorService professorService;

    public TheoryClassController(TokenUtils tokenUtils, CandidateService candidateService, TheoryClassService theoryClassService, ProfessorService professorService) {
        this.tokenUtils = tokenUtils;
        this.candidateService = candidateService;
        this.theoryClassService = theoryClassService;
        this.professorService = professorService;
    }

    @GetMapping("/candidateschedule")
    public ResponseEntity<List<CandidateTheoryClassDTO>> getCandidateFullSchedule(HttpServletRequest request) {
        String token = tokenUtils.getToken(request);
        String email = tokenUtils.getEmailFromToken(token);
        Candidate c = candidateService.findByEmail(email);

        return ResponseEntity.ok(theoryClassService.getFullScheduleDTO(c));
    }

    @GetMapping("/profschedule")
    public ResponseEntity<List<TheoryClassInfoDTO>> getProfessorFullSchedule(HttpServletRequest request) {
        String token = tokenUtils.getToken(request);
        String email = tokenUtils.getEmailFromToken(token);
        Professor p = professorService.findByEmail(email);
        return ResponseEntity.ok(theoryClassService.getProfessorClasses(p));
    }

    @GetMapping("/fullschedule")
    public ResponseEntity<List<TheoryClassInfoDTO>> getAllClasses() {
        return ResponseEntity.ok(theoryClassService.getAllClasses());
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



}
