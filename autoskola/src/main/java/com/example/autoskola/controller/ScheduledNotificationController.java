package com.example.autoskola.controller;

import com.example.autoskola.dto.AdminNotificationDTO;
import com.example.autoskola.dto.ScheduledNotifDTO;
import com.example.autoskola.model.Candidate;
import com.example.autoskola.model.User;
import com.example.autoskola.service.CandidateService;
import com.example.autoskola.service.ScheduledNotificationService;
import com.example.autoskola.util.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/professor")
    @PreAuthorize("hasRole('ROLE_PROFESSOR')")
    public ResponseEntity<List<AdminNotificationDTO>> getProfessorNotifications(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Long userId = user.getId();

        return ResponseEntity.ok(scheduledNotificationService.getNotificationsForUser(userId));
    }

    @GetMapping("/getcandidate")
    public ResponseEntity<List<ScheduledNotifDTO>> getCandidateNotifs(HttpServletRequest req) {
        String token = tokenUtils.getToken(req);
        String email = tokenUtils.getEmailFromToken(token);
        Candidate candidate = candidateService.findByEmail(email);

        return ResponseEntity.ok(scheduledNotificationService.getCandidateClassNotif(candidate));
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<AdminNotificationDTO>> getMyNotifications(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Long userId = user.getId();

        return ResponseEntity.ok(scheduledNotificationService.getNotificationsForUser(userId));
    }

    @PostMapping("/check-now")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> checkNow() {
        scheduledNotificationService.checkInstructorLicenses();
        scheduledNotificationService.checkVehicleRegistrations();
        return ResponseEntity.ok("Notifications checked and created if needed");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteNotification(@PathVariable Long id) {
        scheduledNotificationService.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
