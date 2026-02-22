package com.example.autoskola.controller;

import com.example.autoskola.dto.*;
import com.example.autoskola.model.Candidate;
import com.example.autoskola.model.PracticalClass;
import com.example.autoskola.model.TheoryClass;
import com.example.autoskola.model.User;
import com.example.autoskola.service.*;
import com.example.autoskola.util.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/candidates")
public class CandidateController {
    @Autowired
    private CandidateService candidateService;

    @Autowired
    private UserService userService;

    @Autowired
    private PreferenceService preferenceService;
    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private TheoryClassService theoryClassService;

    @Autowired
    private PracticalClassService practicalClassService;

    @PutMapping("/update/{id}")
    public ResponseEntity<CandidateProfileDTO> updateCandidateProfile(@PathVariable Long id, @RequestBody UpdateCandidateProfileDTO candidateDTO) {

        CandidateProfileDTO response = candidateService.updateProfile(id, candidateDTO);
        return ResponseEntity.ok(response);

    }

    @GetMapping("/myprofile")
    public ResponseEntity<CandidateProfileDTO> getMyProfile(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        CandidateProfileDTO dto = candidateService.getMyProfile(user.getId());

        return ResponseEntity.ok(dto);
    }

    @PutMapping("/myprofile")
    public ResponseEntity<CandidateProfileDTO> updateMyProfile(
            @RequestBody UpdateCandidateProfileDTO dto,
            Authentication authentication) {

        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required");
        }

        User user = (User) authentication.getPrincipal();

        System.out.println("Updating profile for user ID: " + user.getId());
        System.out.println("DTO: " + dto.getFirstName() + " " + dto.getLastName());

        CandidateProfileDTO response = candidateService.updateProfile(user.getId(), dto);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/preferences")
    public ResponseEntity<PreferencesDTO> getPreferences(Authentication authentication) {
        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required");
        }

        User user = (User) authentication.getPrincipal();
        PreferencesDTO preferences = preferenceService.getPreferences(user.getId());

        return ResponseEntity.ok(preferences);
    }

    // UPDATE
    @PutMapping("/preferences")
    public ResponseEntity<PreferencesDTO> updatePreferences(
            @RequestBody PreferencesUpdateDTO dto,
            Authentication authentication) {

        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required");
        }

        User user = (User) authentication.getPrincipal();
        PreferencesDTO response = preferenceService.updatePreferences(user.getId(), dto);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/getstatus")
    public ResponseEntity<String> getCandidateStatus(HttpServletRequest request) {
        String token= tokenUtils.getToken(request);
        String email = tokenUtils.getEmailFromToken(token);
        Candidate candidate = candidateService.findByEmail(email);
        return ResponseEntity.ok(candidate.getStatus().toString());
    }

    @GetMapping("/progress/theory")
    public ResponseEntity<TheoryProgressDTO> getTheoryProgress(Authentication authentication) {
        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required");
        }

        User user = (User) authentication.getPrincipal();
        Candidate candidate = candidateService.findById(user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidate not found"));

        List<TheoryClass> attendedClasses = theoryClassService
                .findByStudentsContainingAndEndTimeBefore(candidate, LocalDateTime.now());

        int attended = attendedClasses.size();
        double percentage = (attended * 100.0) / 40;

        TheoryProgressDTO progress = new TheoryProgressDTO(attended, 40, percentage);

        return ResponseEntity.ok(progress);
    }

    @GetMapping("/progress/practical")
    public ResponseEntity<PracticalProgressDTO> getPracticalProgress(Authentication authentication) {
        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required");
        }

        User user = (User) authentication.getPrincipal();
        Candidate candidate = candidateService.findById(user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidate not found"));

        List<PracticalClass> attendedClasses = practicalClassService
                .findByCandidateAndAcceptedTrueAndEndTimeBefore(candidate, LocalDateTime.now());

        int attended = attendedClasses.size();
        double percentage = (attended * 100.0) / 40;

        PracticalProgressDTO progress = new PracticalProgressDTO(attended, 40, percentage);

        return ResponseEntity.ok(progress);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<CandidateProfileDTO>> getAllCandidates(Authentication authentication) {
        List<CandidateProfileDTO> candidates = candidateService.getAllCandidates();
        return ResponseEntity.ok(candidates);
    }
}
