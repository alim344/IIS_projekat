package com.example.autoskola.repository;

import com.example.autoskola.model.TimePreference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimePreferenceRepository extends JpaRepository<TimePreference, Long> {

    List<TimePreference> findByCandidate_IdIn(List<Long> candidateIds);
}
