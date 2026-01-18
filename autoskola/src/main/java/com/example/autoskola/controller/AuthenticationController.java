package com.example.autoskola.controller;

import com.example.autoskola.dto.AuthenticationRequestDTO;
import com.example.autoskola.dto.AuthenticationResponseDTO;
import com.example.autoskola.dto.RegistrationDTO;
import com.example.autoskola.model.Candidate;
import com.example.autoskola.model.User;
import com.example.autoskola.model.VerificationToken;
import com.example.autoskola.service.CandidateService;
import com.example.autoskola.service.RegistrationService;
import com.example.autoskola.service.UserService;
import com.example.autoskola.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import com.example.autoskola.model.Role;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {


    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private CandidateService candidateService;

    @Autowired
    private RegistrationService registrationService;


    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegistrationDTO registrationDTO) {

        User existUser = userService.getByEmail(registrationDTO.getEmail());
        if (existUser != null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }


        Candidate candidate = candidateService.save(registrationDTO);
        registrationService.verifyMail(candidate);

        return new ResponseEntity<>(candidate, HttpStatus.CREATED);
    }


    @GetMapping("/verify")
    public ResponseEntity<String>  verify(@RequestParam("token") String token) {

        VerificationToken verificationToken = registrationService.getVerificationToken(token);
        if (verificationToken == null) {
            return ResponseEntity.badRequest().body("Invalid token.");
        }
        if(verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body("Token expired.");
        }

        User user = verificationToken.getUser();
        user.setEnabled(true);
        userService.update(user);
        registrationService.deleteToken(verificationToken);
        return ResponseEntity.ok("Account successfully activated.");
    }

    @PostMapping("signin")
    public ResponseEntity<AuthenticationResponseDTO> signin(@RequestBody AuthenticationRequestDTO request){

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = (User) authentication.getPrincipal();
        String jwt = tokenUtils.generateToken(user.getEmail());
        int expiresIn = tokenUtils.getExpiredIn();



        return ResponseEntity.ok(new AuthenticationResponseDTO(jwt, expiresIn));


    }



}
