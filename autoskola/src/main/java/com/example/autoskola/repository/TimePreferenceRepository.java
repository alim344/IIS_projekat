package com.example.autoskola.repository;

import com.example.autoskola.model.TimePreference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TimePreferenceRepository extends JpaRepository<TimePreference, Long> {

    List<TimePreference> findByCandidate_IdIn(List<Long> candidateIds);

    Optional<TimePreference> findByCandidateId(Long candidateId);
}
