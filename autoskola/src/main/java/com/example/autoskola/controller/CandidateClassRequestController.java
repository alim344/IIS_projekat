package com.example.autoskola.controller;

import com.example.autoskola.dto.ChangePClassRequestDTO;
import com.example.autoskola.service.CandidateClassRequestService;
import com.example.autoskola.util.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/request")
public class CandidateClassRequestController {

    @Autowired
    private CandidateClassRequestService requestService;

    @Autowired
    private TokenUtils tokenUtils;

    @PostMapping("/save")
    public ResponseEntity<String> saveRequest(@RequestBody ChangePClassRequestDTO dto, HttpServletRequest request) {

        String token = tokenUtils.getToken(request);
        String candidate_email = tokenUtils.getEmailFromToken(token);

        requestService.saveFromDTO(dto,candidate_email);
        return ResponseEntity.ok("Request sent");
    }
}
