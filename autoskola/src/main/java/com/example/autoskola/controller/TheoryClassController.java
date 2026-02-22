package com.example.autoskola.controller;

import com.example.autoskola.dto.*;
import com.example.autoskola.model.*;
import com.example.autoskola.repository.TheoryClassRepository;
import com.example.autoskola.repository.TheoryLessonRepository;
import com.example.autoskola.service.*;
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
import java.time.LocalTime;
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
    private final TheoryLessonService theoryLessonService;


    public TheoryClassController(TokenUtils tokenUtils, CandidateService candidateService, TheoryClassService theoryClassService, ProfessorService professorService, TheorySchedulingService theorySchedulingService, TheoryClassRepository theoryClassRepository, TheoryLessonService theoryLessonService) {
        this.tokenUtils = tokenUtils;
        this.candidateService = candidateService;
        this.theoryClassService = theoryClassService;
        this.professorService = professorService;
        this.theorySchedulingService = theorySchedulingService;
        this.theoryClassRepository = theoryClassRepository;
        this.theoryLessonService = theoryLessonService;
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

    @PostMapping("/manual")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createManualClass(@RequestBody ManualTheoryClassRequestDTO request) {
        try {
            TheoryClass created = theorySchedulingService.createManualClass(request);
            return ResponseEntity.ok(theoryClassService.toDTO(created));
        } catch (Exception e) {
            Map<String, String> err = new HashMap<>();
            err.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(err);
        }
    }

    @GetMapping("/candidates/available")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> getAvailableCandidates(@RequestParam FixedSlot slot) {
        List<Candidate> allInTheory = candidateService.findByStatus(TrainingStatus.THEORY);

        List<Map<String, Object>> result = allInTheory.stream()
                .filter(c -> {
                    if (c.getTimePreference() == null) return true; // bez preference — uvek dostupan
                    LocalTime prefStart = c.getTimePreference().getStartTime();
                    LocalTime prefEnd = c.getTimePreference().getEndTime();
                    return !slot.getStartTime().isBefore(prefStart) && !slot.getEndTime().isAfter(prefEnd);
                })
                .map(c -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("id", c.getId());
                    m.put("name", c.getName());
                    m.put("lastname", c.getLastname());
                    m.put("category", c.getCategory());
                    m.put("attendedLessons", c.getAttendedLessons().size());
                    m.put("hasPreference", c.getTimePreference() != null);
                    m.put("preferenceStart", c.getTimePreference() != null ? c.getTimePreference().getStartTime() : null);
                    m.put("preferenceEnd", c.getTimePreference() != null ? c.getTimePreference().getEndTime() : null);
                    return m;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @GetMapping("/theory-lessons")
    public ResponseEntity<List<TheoryLessonDTO>> getAllLessons() {
        List<TheoryLesson> lessons = theoryLessonService.findAllByOrderByOrderNumberAsc();
        List<TheoryLessonDTO> dtos = lessons.stream()
                .map(l -> {
                    TheoryLessonDTO dto = new TheoryLessonDTO();
                    dto.setId(l.getId());
                    dto.setName(l.getName());
                    dto.setOrderNumber(l.getOrderNumber());
                    return dto;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
}
