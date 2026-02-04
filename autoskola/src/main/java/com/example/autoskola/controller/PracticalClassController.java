package com.example.autoskola.controller;

import com.example.autoskola.model.PracticalClass;
import com.example.autoskola.service.CandidateService;
import com.example.autoskola.service.InstructorService;
import com.example.autoskola.service.PracticalClassService;
import com.example.autoskola.util.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/nextWeek/instructor")
    public ResponseEntity<List<PracticalClass>> getNextWeeksInstructorClasses( @RequestParam long instructorId){
        return ResponseEntity.ok(practicalClassService.getInstructorNextWeekClasses(instructorId));
    }

    @GetMapping("/instructor/nextschedule")
    public ResponseEntity<List<PracticalClass>> getInstructorNextWeekClasses(HttpServletRequest request){
        String token= tokenUtils.getToken(request);
        String email = tokenUtils.getEmailFromToken(token);
        long instructorId = instructorService.getIdByEmail(email);


        return ResponseEntity.ok(practicalClassService.getInstructorNextWeekClasses(instructorId));
    }

    @GetMapping("/instructor/thisschedule")
    public ResponseEntity<List<PracticalClass>> getInstructorThisWeekClasses(HttpServletRequest request){

        String token= tokenUtils.getToken(request);
        String email = tokenUtils.getEmailFromToken(token);
        long instructorId = instructorService.getIdByEmail(email);
        return ResponseEntity.ok(practicalClassService.getInstructorThisWeekClasses(instructorId));
    }


    @GetMapping("/candidate/nextschedlue")
    public ResponseEntity<List<PracticalClass>> getCandidateNextWeekPracticalClasses(HttpServletRequest request){
        String token= tokenUtils.getToken(request);
        String email = tokenUtils.getEmailFromToken(token);
        long candidateId = candidateService.getIdByEmail(email);
        return ResponseEntity.ok(practicalClassService.getCandidateNextWeekPracticalClasses(candidateId));
    }


    @GetMapping("/candidate/thisschedlue")
    public ResponseEntity<List<PracticalClass>> getCandidateThisWeekPracticalClasses(HttpServletRequest request){
        String token= tokenUtils.getToken(request);
        String email = tokenUtils.getEmailFromToken(token);
        long candidateId = candidateService.getIdByEmail(email);
        return ResponseEntity.ok(practicalClassService.getCandidateThisWeekPracticalClasses(candidateId));
    }


}
