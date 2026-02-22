package com.example.autoskola.repository;

import com.example.autoskola.model.PracticalExam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PracticalExamRepository extends JpaRepository<PracticalExam, Long> {

    List<PracticalExam> findByInstructorId(Long instructorId);
}
