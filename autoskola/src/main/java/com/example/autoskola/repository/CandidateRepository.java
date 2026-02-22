package com.example.autoskola.repository;

import com.example.autoskola.model.Candidate;
import com.example.autoskola.model.TrainingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {

        Candidate save(Candidate candidate);
        Candidate getByEmail(String email);
        Optional<Candidate> findById(Long id);

        List<Candidate> getByInstructorId(Long instructorId);

        List<Candidate> findByTheoryCompletedTrueAndInstructorIsNull();

        List<Candidate> findByStatus(TrainingStatus status);

        long countByStatus(TrainingStatus status);

        List<Candidate> findByStatusAndTheoryCompletedAndInstructorIsNotNull(
                TrainingStatus status, boolean theoryCompleted);
}
