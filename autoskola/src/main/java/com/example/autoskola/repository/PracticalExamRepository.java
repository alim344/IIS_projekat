package com.example.autoskola.repository;

import com.example.autoskola.model.PracticalExam;
import com.example.autoskola.model.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PracticalExamRepository extends JpaRepository<PracticalExam, Long> {

    List<PracticalExam> findByInstructorId(Long instructorId);

    @Query("SELECT COUNT(p) FROM PracticalExam p WHERE p.professor = :professor AND p.dateTime BETWEEN :start AND :end")
    int countByProfessorAndDateTimeBetween(@Param("professor") Professor professor,
                                           @Param("start") LocalDateTime start,
                                           @Param("end") LocalDateTime end);

    List<PracticalExam> findByProfessorIdAndDateTimeBetween(Long professorId,
                                                            LocalDateTime start,
                                                            LocalDateTime end);

    List<PracticalExam> findByCandidateIdAndDateTimeBetween(Long candidateId,
                                                            LocalDateTime start,
                                                            LocalDateTime end);


    List<PracticalExam> findByProfessorId(Long professorId);

}
