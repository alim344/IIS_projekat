package com.example.autoskola.repository;

import com.example.autoskola.model.Candidate;
import com.example.autoskola.model.PracticalClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface PracticalClassRepository extends JpaRepository<PracticalClass, Long> {

    List<PracticalClass> findByInstructorIdAndStartTimeBetween( Long instructorId,
                                                                LocalDateTime startOfNextWeek,
                                                                LocalDateTime startOfWeekAfterNext);
    List<PracticalClass> findByCandidateIdAndStartTimeBetween(Long candidateId,
                                                              LocalDateTime startOfNextWeek,
                                                              LocalDateTime startOfWeekAfterNext);
    List<PracticalClass> findByInstructorId(Long instructorId);

    void deleteById(Long id);

    boolean existsById(long id);

    PracticalClass save(PracticalClass p);

    PracticalClass findById(long id);

    List<PracticalClass> findByCandidateId(Long candidateId);

    List<PracticalClass> findByCandidateAndAcceptedTrueAndEndTimeBefore(Candidate candidate, LocalDateTime time);

     @Query("""
        SELECT COUNT(c) > 0 FROM PracticalClass c
        WHERE c.instructor.id = :instId
        AND c.startTime < :end
        AND c.endTime > :start
        """)
    boolean existsOverlap(
            Long instId,
            LocalDateTime start,
            LocalDateTime end
    );


}
