package com.example.autoskola.repository;

import com.example.autoskola.model.PracticalClass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PracticalClassRepository extends JpaRepository<PracticalClass, Long> {

    List<PracticalClass> findByInstructorIdAndStartTimeBetween( Long instructorId,
                                                                LocalDateTime startOfNextWeek,
                                                                LocalDateTime startOfWeekAfterNext);
    List<PracticalClass> findByCandidateIdAndStartTimeBetween(Long candidateId,
                                                              LocalDateTime startOfNextWeek,
                                                              LocalDateTime startOfWeekAfterNext);
}
