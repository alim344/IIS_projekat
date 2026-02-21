package com.example.autoskola.repository;

import com.example.autoskola.model.TheoryExam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TheoryExamRepository extends JpaRepository<TheoryExam, Long> {
    List<TheoryExam> findByRegisteredById(Long professorId);
}
