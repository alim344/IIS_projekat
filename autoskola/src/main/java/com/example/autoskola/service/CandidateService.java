package com.example.autoskola.service;

import com.example.autoskola.dto.CandidateProfileDTO;
import com.example.autoskola.dto.PracticalDTO;
import com.example.autoskola.dto.RegistrationDTO;
import com.example.autoskola.dto.UpdateCandidateProfileDTO;
import com.example.autoskola.model.Candidate;
import com.example.autoskola.model.PracticalClass;
import com.example.autoskola.model.TrainingStatus;
import com.example.autoskola.repository.CandidateRepository;
import com.example.autoskola.repository.PracticalClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CandidateService {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private PracticalClassRepository practicalClassRepository;

    public Candidate saveFromDTO(RegistrationDTO registrationDTO) {

        Candidate c = new Candidate();

        c.setName(registrationDTO.getFirstName());
        c.setLastname(registrationDTO.getLastName());
        c.setEmail(registrationDTO.getEmail());
        c.setStartOfTraining(LocalDateTime.now());  // da li ocemo da bude now ili admin klikne kad cel agurpa krene
        c.setUsername(registrationDTO.getUsername());
        c.setCategory(registrationDTO.getCategory());
        c.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        c.setRole(roleService.findByName("ROLE_CANDIDATE"));
        return candidateRepository.save(c);

    }

    public Candidate save(Candidate c){
        return candidateRepository.save(c);
    }

    public long getIdByEmail(String email) {
        Candidate c = candidateRepository.getByEmail(email);
        return c.getId();
    }

    public List<Candidate> getByInstructorId(long instructorId) {
        return candidateRepository.getByInstructorId(instructorId);
    }
    

    public CandidateProfileDTO updateProfile(Long id, UpdateCandidateProfileDTO dto) {
        System.out.println("=== UPDATE PROFILE SERVICE ===");
        System.out.println("ID: " + id);
        System.out.println("DTO: " + dto.getFirstName() + " " + dto.getLastName());


        Candidate candidate = candidateRepository.findById(id).orElseThrow(() -> new RuntimeException("Candidate not found."));

        if (dto.getFirstName() != null)
            candidate.setName(dto.getFirstName());

        if (dto.getLastName() != null)
            candidate.setLastname(dto.getLastName());

        if (dto.getEmail() != null)
            candidate.setEmail(dto.getEmail());

        if (dto.getUsername() != null)
            candidate.setUsername(dto.getUsername());

        Candidate saved = candidateRepository.save(candidate);

        CandidateProfileDTO response = new CandidateProfileDTO(saved);

        System.out.println("Response ID: " + response.getCandidateId());
        System.out.println("Response name: " + response.getName());

        return response;
    }

    public CandidateProfileDTO getMyProfile(Long userId) {
        Candidate candidate = candidateRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Candidate not found"
                ));

        return new CandidateProfileDTO(candidate);
    }

    public Optional<Candidate> findById(Long id) {
        return candidateRepository.findById(id);
    }

    public Candidate findByEmail(String email){
        return candidateRepository.getByEmail(email);
    }


}
