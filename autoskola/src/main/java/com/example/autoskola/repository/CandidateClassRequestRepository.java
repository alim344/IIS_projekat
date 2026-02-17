package com.example.autoskola.repository;

import com.example.autoskola.model.CandidateClassRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateClassRequestRepository extends JpaRepository<CandidateClassRequest,Long> {
    CandidateClassRequest save(CandidateClassRequest candidateClassRequest);
}
