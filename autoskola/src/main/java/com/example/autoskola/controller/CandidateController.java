package com.example.autoskola.controller;

import com.example.autoskola.dto.CandidateProfileDTO;
import com.example.autoskola.dto.PreferencesDTO;
import com.example.autoskola.dto.PreferencesUpdateDTO;
import com.example.autoskola.dto.UpdateCandidateProfileDTO;
import com.example.autoskola.model.Candidate;
import com.example.autoskola.model.User;
import com.example.autoskola.service.CandidateService;
import com.example.autoskola.service.PreferenceService;
import com.example.autoskola.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/candidates")
public class CandidateController {
    @Autowired
    private CandidateService candidateService;

    @Autowired
    private UserService userService;

    @Autowired
    private PreferenceService preferenceService;

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
}
