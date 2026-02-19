package com.example.autoskola.repository;

import com.example.autoskola.model.Professor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {

    Professor save(Professor professor);
    Professor findByEmail(String email);
}
