package com.example.autoskola.repository;

import com.example.autoskola.model.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateRepository extends JpaRepository<Candidate, Integer> {

        Candidate save(Candidate candidate);
}
