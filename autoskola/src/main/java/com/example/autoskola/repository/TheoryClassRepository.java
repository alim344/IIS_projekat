package com.example.autoskola.repository;

import com.example.autoskola.model.TheoryClass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TheoryClassRepository extends JpaRepository<TheoryClass, Long> {

    List<TheoryClass> findAll();

    List<TheoryClass> findByStudents_Id(Long candidateId);

    TheoryClass findById(long id);
}
