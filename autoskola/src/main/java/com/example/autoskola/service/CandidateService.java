package com.example.autoskola.service;

import com.example.autoskola.dto.RegistrationDTO;
import com.example.autoskola.model.Candidate;
import com.example.autoskola.model.TrainingStatus;
import com.example.autoskola.repository.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CandidateService {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Candidate save(RegistrationDTO registrationDTO) {

        Candidate c = new Candidate();

        c.setName(registrationDTO.getFirstName());
        c.setLastname(registrationDTO.getLastName());
        c.setEmail(registrationDTO.getEmail());
        c.setStartOfTraining(LocalDateTime.now());  // da li ocemo da bude now ili admin klikne kad cel agurpa krene
        c.setUsername(registrationDTO.getUsername());
        c.setCategory(registrationDTO.getCategory());
        c.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        c.setRoles(roleService.findByName("ROLE_CANDIDATE"));;
        return candidateRepository.save(c);

    }

}
