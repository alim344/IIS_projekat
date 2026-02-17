package com.example.autoskola.service;

import com.example.autoskola.dto.ChangePClassRequestDTO;
import com.example.autoskola.dto.InstPracticalRequestDTO;
import com.example.autoskola.model.Candidate;
import com.example.autoskola.model.CandidateClassRequest;
import com.example.autoskola.model.CandidateClassRequestStatus;
import com.example.autoskola.model.Instructor;
import com.example.autoskola.repository.CandidateClassRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    public List<InstPracticalRequestDTO> getInstructorRequests(long instructor_id) {

        List<CandidateClassRequest> requests = candidateClassRequestRepository.findByInstructorId(instructor_id);
        List<InstPracticalRequestDTO> dtos = new ArrayList<>();

        for (CandidateClassRequest request : requests) {

            if(request.getStatus() == CandidateClassRequestStatus.PENDING) {
                InstPracticalRequestDTO dto = new InstPracticalRequestDTO();
                dto.setText(request.getText());
                dto.setStatus(request.getStatus().toString());
                dto.setId(request.getId());
                Candidate candidate = request.getCandidate();
                dto.setEmail(candidate.getEmail());
                dto.setCandidate_name(candidate.getName());
                dto.setCandidate_lastname(candidate.getLastname());
                dtos.add(dto);
            }

        }
        return dtos;
    }

    public void acceptRequest(long request_id) {
        CandidateClassRequest candidateClassRequest = candidateClassRequestRepository.findById(request_id);
        if(candidateClassRequest.getStatus() == CandidateClassRequestStatus.PENDING) {
            candidateClassRequest.setStatus(CandidateClassRequestStatus.ACCEPTED);
            save(candidateClassRequest);
        }
    }

    public void declineRequest(long request_id) {
        CandidateClassRequest candidateClassRequest = candidateClassRequestRepository.findById(request_id);
        if(candidateClassRequest.getStatus() == CandidateClassRequestStatus.PENDING) {
            candidateClassRequest.setStatus(CandidateClassRequestStatus.DECLINED);
            save(candidateClassRequest);
        }
    }


}
