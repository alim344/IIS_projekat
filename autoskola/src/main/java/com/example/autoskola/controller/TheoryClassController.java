package com.example.autoskola.controller;

import com.example.autoskola.dto.AttendanceTheorySubmitDTO;
import com.example.autoskola.dto.CandidateTheoryClassDTO;
import com.example.autoskola.dto.TheoryClassAdminInfoDTO;
import com.example.autoskola.dto.TheoryClassInfoDTO;
import com.example.autoskola.model.Candidate;
import com.example.autoskola.model.Professor;
import com.example.autoskola.model.TheoryClass;
import com.example.autoskola.repository.TheoryClassRepository;
import com.example.autoskola.repository.TheoryLessonRepository;
import com.example.autoskola.service.CandidateService;
import com.example.autoskola.service.ProfessorService;
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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private final ProfessorService professorService;
    private final TheoryClassRepository theoryClassRepository;


    public TheoryClassController(TokenUtils tokenUtils, CandidateService candidateService, TheoryClassService theoryClassService, ProfessorService professorService, TheorySchedulingService theorySchedulingService, TheoryClassRepository theoryClassRepository) {
        this.tokenUtils = tokenUtils;
        this.candidateService = candidateService;
        this.theoryClassService = theoryClassService;
        this.professorService = professorService;
        this.theorySchedulingService = theorySchedulingService;
        this.theoryClassRepository = theoryClassRepository;
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

    @PatchMapping("/leave/{classId}")
    public ResponseEntity<String> leave(@PathVariable long classId,HttpServletRequest request) {
        String token = tokenUtils.getToken(request);
        String email = tokenUtils.getEmailFromToken(token);
        Candidate c = candidateService.findByEmail(email);
        theoryClassService.leave(c,classId);
        return ResponseEntity.ok("You left this class!!");
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

    @GetMapping("/this-week")
    public ResponseEntity<List<TheoryClassAdminInfoDTO>> getThisWeekClasses(HttpServletRequest request) {
        String token = tokenUtils.getToken(request);
        String email = tokenUtils.getEmailFromToken(token);

        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        LocalDateTime start = startOfWeek.atStartOfDay();
        LocalDateTime end = endOfWeek.atTime(23, 59, 59);

        List<TheoryClass> classes = theoryClassRepository.findByStartTimeBetween(start, end);
        List<TheoryClassAdminInfoDTO> dtos = classes.stream()
                .map(theoryClassService::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/next-week")
    public ResponseEntity<List<TheoryClassAdminInfoDTO>> getNextWeekClasses(HttpServletRequest request) {
        String token = tokenUtils.getToken(request);
        String email = tokenUtils.getEmailFromToken(token);

        LocalDate today = LocalDate.now();
        LocalDate nextMonday = today.with(DayOfWeek.MONDAY).plusWeeks(1);
        LocalDate nextSunday = nextMonday.plusDays(6);

        LocalDateTime start = nextMonday.atStartOfDay();
        LocalDateTime end = nextSunday.atTime(23, 59, 59);

        List<TheoryClass> classes = theoryClassRepository.findByStartTimeBetween(start, end);
        List<TheoryClassAdminInfoDTO> dtos = classes.stream()
                .map(theoryClassService::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/{classId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Map<String, String>> deleteTheoryClass(@PathVariable Long classId) {
        try {
            theoryClassService.deleteTheoryClass(classId);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Theory class deleted successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error deleting theory class: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/class/{classId}/students")
    public ResponseEntity<?> getClassStudents(
            @PathVariable Long classId,
            HttpServletRequest request) {

        String token = tokenUtils.getToken(request);
        String email = tokenUtils.getEmailFromToken(token);
        Professor professor = professorService.findByEmail(email);

        try {
            TheoryClassAdminInfoDTO dto = theoryClassService.getClassWithStudents(classId, professor.getId());
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/attendance")
    public ResponseEntity<String> submitAttendance(
            @RequestBody AttendanceTheorySubmitDTO dto,
            HttpServletRequest request) {

        String token = tokenUtils.getToken(request);
        String email = tokenUtils.getEmailFromToken(token);
        Professor professor = professorService.findByEmail(email);

        try {
            theoryClassService.submitAttendance(dto.getClassId(), dto.getPresentCandidateIds(), professor.getId());
            return ResponseEntity.ok("Attendance submitted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
