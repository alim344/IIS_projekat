package com.example.autoskola.repository;

import com.example.autoskola.model.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {

        Candidate save(Candidate candidate);
}
