package com.example.autoskola.controller;

import com.example.autoskola.dto.*;
import com.example.autoskola.model.Instructor;
import com.example.autoskola.model.PracticalClass;
import com.example.autoskola.service.*;
import com.example.autoskola.util.TokenUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/practicalclass")
public class PracticalClassController {

    @Autowired
    public PracticalClassService practicalClassService;
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private InstructorService instructorService;
    @Autowired
    private CandidateService candidateService;
    @Autowired
    private ScheduledNotificationService scheduledNotificationService;
    @Autowired
    private InstructorScheduleGenerator instructorScheduleGenerator;

    @GetMapping("/nextWeek/instructor")
    public ResponseEntity<List<PracticalClass>> getNextWeeksInstructorClasses(@RequestParam long instructorId) {
        return ResponseEntity.ok(practicalClassService.getInstructorNextWeekClasses(instructorId));
    }

    @GetMapping("/instructor/nextschedule")
    public ResponseEntity<List<PracticalClass>> getInstructorNextWeekClasses(HttpServletRequest request) {
        String token = tokenUtils.getToken(request);
        String email = tokenUtils.getEmailFromToken(token);
        long instructorId = instructorService.getIdByEmail(email);


        return ResponseEntity.ok(practicalClassService.getInstructorNextWeekClasses(instructorId));
    }

    @GetMapping("/instructor/thisschedule")
    public ResponseEntity<List<PracticalClass>> getInstructorThisWeekClasses(HttpServletRequest request) {

        String token = tokenUtils.getToken(request);
        String email = tokenUtils.getEmailFromToken(token);
        long instructorId = instructorService.getIdByEmail(email);
        return ResponseEntity.ok(practicalClassService.getInstructorThisWeekClasses(instructorId));
    }


    @GetMapping("/candidate/nextschedlue")
    public ResponseEntity<List<PracticalClass>> getCandidateNextWeekPracticalClasses(HttpServletRequest request) {
        String token = tokenUtils.getToken(request);
        String email = tokenUtils.getEmailFromToken(token);
        long candidateId = candidateService.getIdByEmail(email);
        return ResponseEntity.ok(practicalClassService.getCandidateNextWeekPracticalClasses(candidateId));
    }


    @GetMapping("/candidate/thisschedlue")
    public ResponseEntity<List<PracticalClass>> getCandidateThisWeekPracticalClasses(HttpServletRequest request) {
        String token = tokenUtils.getToken(request);
        String email = tokenUtils.getEmailFromToken(token);
        long candidateId = candidateService.getIdByEmail(email);
        return ResponseEntity.ok(practicalClassService.getCandidateThisWeekPracticalClasses(candidateId));
    }

    @GetMapping("/fullschedule")
    public ResponseEntity<List<PracticalDTO>> getInstructorClasses(HttpServletRequest request) {
        String token = tokenUtils.getToken(request);
        String email = tokenUtils.getEmailFromToken(token);
        long instructorId = instructorService.getIdByEmail(email);
        return ResponseEntity.ok(practicalClassService.getInstructorSchedule(instructorId));
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable long id) {
        practicalClassService.deleteClass(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/updateDateTime")
    public ResponseEntity<PracticalDTO> updateClass(@RequestBody PracticalDTO dto) {

        PracticalDTO pclass = practicalClassService.updateDateTime(dto);

        if (pclass == null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        return ResponseEntity.ok(pclass);

    }

    @PostMapping("/manual_schedule/save")
    public ResponseEntity<String> saveManualSchedule(@RequestBody List<DraftPracticalClassDTO> dtos, HttpServletRequest request) {

        String token = tokenUtils.getToken(request);
        String email = tokenUtils.getEmailFromToken(token);
        Instructor i = instructorService.findByEmail(email);

        practicalClassService.saveManualSchedule(dtos, i);
        return ResponseEntity.ok("saved");
    }

    @PostMapping("/saveClass")
    public ResponseEntity<DraftPracticalClassDTO> saveClass(@RequestBody DraftPracticalClassDTO dto, @RequestParam(required = false) Long requestId, HttpServletRequest request) {
        String token = tokenUtils.getToken(request);
        String email = tokenUtils.getEmailFromToken(token);
        Instructor i = instructorService.findByEmail(email);

        if (dto.getStartTime() == null || dto.getEndTime() == null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return ResponseEntity.ok(practicalClassService.saveByDraftDTO(dto, i, requestId));
    }


    @GetMapping("/getCopiedSchedule")
    public ResponseEntity<List<PracticalDTO>> getCopiedSchedule(HttpServletRequest request) {

        String token = tokenUtils.getToken(request);
        String email = tokenUtils.getEmailFromToken(token);
        long instructorId = instructorService.getIdByEmail(email);

        List<PracticalDTO> dtos = practicalClassService.getCopiedSchedule(instructorId);

        if (dtos == null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        return ResponseEntity.ok(dtos);
    }


    @GetMapping("/candidate/fullschedule")
    public ResponseEntity<List<CandidatePracticalDTO>> getCandidateSchedule(HttpServletRequest request) {
        String token = tokenUtils.getToken(request);
        String email = tokenUtils.getEmailFromToken(token);
        long id = candidateService.getIdByEmail(email);

        return ResponseEntity.ok(practicalClassService.getCandidateSchedule(id));
    }

    @PutMapping("/{classId}/record")
    public ResponseEntity<PracticalDTO> recordClass(@PathVariable long classId, @RequestBody RecordPracticalDTO dto, HttpServletRequest request) {
        String token = tokenUtils.getToken(request);
        String email = tokenUtils.getEmailFromToken(token);
        long instructorId = instructorService.getIdByEmail(email);

        PracticalDTO updated = practicalClassService.saveRecordPracticalClass(classId, instructorId, dto);
        return ResponseEntity.ok(updated);
    }


    @PatchMapping("/acceptClass/{classId}")
    public ResponseEntity<String> acceptPracticalClass(@PathVariable long classId) {
        practicalClassService.acceptClass(classId);
        return ResponseEntity.ok("Class accepted");
    }

    @GetMapping("/admin/instructor/{instructorId}/schedule")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<PracticalDTO>> getInstructorScheduleForAdmin(@PathVariable Long instructorId) {
        List<PracticalDTO> schedule = practicalClassService.getInstructorSchedule(instructorId);
        return ResponseEntity.ok(schedule);


    }


    @PostMapping("/schedule/alg")
    public ResponseEntity<List<PracticalDTO>> generateSchedule(@RequestBody GeneratorDTO dto,HttpServletRequest request) {

        String token = tokenUtils.getToken(request);
        String email = tokenUtils.getEmailFromToken(token);
        Instructor i = instructorService.findByEmail(email);
        if(dto.getEmails().size() > 12){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        boolean available = instructorScheduleGenerator.checkNextWeekAvailability(i.getId());

        if(!available){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        return ResponseEntity.ok(instructorScheduleGenerator.generateSchedule(dto.getLightDays(), dto.getEmails()));
    }


}
