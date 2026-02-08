package com.example.autoskola.repository;

import com.example.autoskola.model.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {

        Candidate save(Candidate candidate);
        Candidate getByEmail(String email);


        List<Candidate> getByInstructorId(Long instructorId);
}
