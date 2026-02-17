package com.example.autoskola.service;

import com.example.autoskola.dto.ChangePClassRequestDTO;
import com.example.autoskola.model.Candidate;
import com.example.autoskola.model.CandidateClassRequest;
import com.example.autoskola.model.CandidateClassRequestStatus;
import com.example.autoskola.model.Instructor;
import com.example.autoskola.repository.CandidateClassRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CandidateClassRequestService {

    @Autowired
    private CandidateClassRequestRepository candidateClassRequestRepository;
    @Autowired
    private InstructorService instructorService;
    @Autowired
    private CandidateService candidateService;

    public void save(CandidateClassRequest candidateClassRequest) {
        candidateClassRequestRepository.save(candidateClassRequest);
    }

    public void saveFromDTO(ChangePClassRequestDTO requestDTO, String candidate_email) {
        CandidateClassRequest request = new CandidateClassRequest();

        Instructor i = instructorService.findByEmail(requestDTO.getInstructorEmail());
        Candidate candidate = candidateService.findByEmail(candidate_email);

        request.setInstructor(i);
        request.setCandidate(candidate);
        request.setStatus(CandidateClassRequestStatus.PENDING);
        request.setText(requestDTO.getText());

        save(request);


    }


}
