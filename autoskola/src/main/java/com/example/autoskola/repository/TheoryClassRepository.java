package com.example.autoskola.repository;

import com.example.autoskola.model.Candidate;
import com.example.autoskola.model.TheoryClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface TheoryClassRepository extends JpaRepository<TheoryClass, Long> {

    List<TheoryClass> findAll();

    List<TheoryClass> findByStudents_Id(Long candidateId);

    TheoryClass findById(long id);

    List<TheoryClass> findByProfessorId(Long professorId);

    @Query("SELECT COUNT(tc) > 0 FROM TheoryClass tc JOIN tc.students s " +
            "WHERE s.id = :candidateId AND tc.theoryLesson.id = :lessonId")
    boolean isCandidateEnrolledInLesson(@Param("candidateId") Long candidateId,
                                        @Param("lessonId") Long lessonId);

    boolean existsByProfessorIdAndStartTimeBetween(Long professorId, LocalDateTime startTime, LocalDateTime endTime);

    List<TheoryClass> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);

    List<TheoryClass> findByStudentsContainingAndEndTimeBefore(Candidate candidate, LocalDateTime time);
}
