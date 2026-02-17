package com.example.autoskola.controller;

import com.example.autoskola.dto.ChangePClassRequestDTO;
import com.example.autoskola.dto.InstPracticalRequestDTO;
import com.example.autoskola.model.CandidateClassRequest;
import com.example.autoskola.model.Instructor;
import com.example.autoskola.service.CandidateClassRequestService;
import com.example.autoskola.service.InstructorService;
import com.example.autoskola.util.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/request")
public class CandidateClassRequestController {

    @Autowired
    private CandidateClassRequestService requestService;

    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private InstructorService instructorService;

    @PostMapping("/save")
    public ResponseEntity<String> saveRequest(@RequestBody ChangePClassRequestDTO dto, HttpServletRequest request) {

        String token = tokenUtils.getToken(request);
        String candidate_email = tokenUtils.getEmailFromToken(token);

        requestService.saveFromDTO(dto,candidate_email);
        return ResponseEntity.ok("Request sent");
    }


    @GetMapping("/getInstructorRequests")
    public ResponseEntity<List<InstPracticalRequestDTO>> getRequests(HttpServletRequest request) {
        String token = tokenUtils.getToken(request);
        String email = tokenUtils.getEmailFromToken(token);
        long instructor_id = instructorService.getIdByEmail(email);

        return ResponseEntity.ok(requestService.getInstructorRequests(instructor_id));
    }

    @PatchMapping("/acceptRequest/{request_id}")
    public ResponseEntity<String> acceptRequest(@PathVariable long request_id) {
        requestService.acceptRequest(request_id);
        return ResponseEntity.ok("Request accepted");
    }

    @PatchMapping("/declineRequest/{request_id}")
    public ResponseEntity<String> declineRequest(@PathVariable long request_id) {
        requestService.declineRequest(request_id);
        return ResponseEntity.ok("Request declined");
    }





}
