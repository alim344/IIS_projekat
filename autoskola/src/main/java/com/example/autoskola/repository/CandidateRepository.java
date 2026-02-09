package com.example.autoskola.repository;

import com.example.autoskola.model.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {

        Candidate save(Candidate candidate);
        Candidate getByEmail(String email);
        Optional<Candidate> findById(Long id);

        List<Candidate> getByInstructorId(Long instructorId);
}
