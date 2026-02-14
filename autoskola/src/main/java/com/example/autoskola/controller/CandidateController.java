package com.example.autoskola.controller;

import com.example.autoskola.dto.CandidateProfileDTO;
import com.example.autoskola.dto.UpdateCandidateProfileDTO;
import com.example.autoskola.model.Candidate;
import com.example.autoskola.model.User;
import com.example.autoskola.service.CandidateService;
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
}
