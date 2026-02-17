package com.example.autoskola.repository;

import com.example.autoskola.model.CandidateClassRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CandidateClassRequestRepository extends JpaRepository<CandidateClassRequest,Long> {
    CandidateClassRequest save(CandidateClassRequest candidateClassRequest);

    CandidateClassRequest findById(long id);
    List<CandidateClassRequest> findByInstructorId(long id);

}

