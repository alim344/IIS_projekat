package com.example.autoskola.controller;

import com.example.autoskola.dto.ScheduledNotifDTO;
import com.example.autoskola.service.CandidateService;
import com.example.autoskola.service.ScheduledNotificationService;
import com.example.autoskola.util.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/schedulednotif")
public class ScheduledNotificationController {


    private final TokenUtils tokenUtils;
    private final CandidateService candidateService;
    private final ScheduledNotificationService scheduledNotificationService;

    public ScheduledNotificationController(TokenUtils tokenUtils, CandidateService candidateService, ScheduledNotificationService scheduledNotificationService) {
        this.tokenUtils = tokenUtils;
        this.candidateService = candidateService;
        this.scheduledNotificationService = scheduledNotificationService;
    }

    @GetMapping("/getcandidate")
    public ResponseEntity<List<ScheduledNotifDTO>> getCandidateNotifs(HttpServletRequest req) {
        String token = tokenUtils.getToken(req);
        String email = tokenUtils.getEmailFromToken(token);
        long candidate_id = candidateService.getIdByEmail(email);

        return ResponseEntity.ok(scheduledNotificationService.getCandidateNotif(candidate_id));
    }

}
