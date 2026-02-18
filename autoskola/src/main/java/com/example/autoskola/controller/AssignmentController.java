package com.example.autoskola.controller;

import com.example.autoskola.dto.AssignmentRequestDTO;
import com.example.autoskola.dto.CandidateAssignmentDTO;
import com.example.autoskola.dto.InstructorWorkloadDTO;
import com.example.autoskola.service.AssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/assignments")
@RequiredArgsConstructor
public class AssignmentController {

    private final AssignmentService assignmentService;

    @GetMapping("/pending")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<CandidateAssignmentDTO>> getPendingAssignments() {
        return ResponseEntity.ok(assignmentService.getPendingAssignments());
    }

    @GetMapping("/instructor-workload")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<InstructorWorkloadDTO>> getInstructorWorkload() {
        return ResponseEntity.ok(assignmentService.getInstructorWorkload());
    }

    @PostMapping("/propose")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Map<Long, Long>> proposeAssignment(@RequestBody List<Long> candidateIds) {
        return ResponseEntity.ok(assignmentService.proposeAssignment(candidateIds));
    }
    
    @PostMapping("/save")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> saveAssignment(@RequestBody AssignmentRequestDTO request) {
        try {
            assignmentService.saveAssignment(request);
            return ResponseEntity.ok("Assignment saved successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
