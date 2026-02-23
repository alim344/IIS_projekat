package com.example.autoskola.repository;

import com.example.autoskola.model.Instructor;
import com.example.autoskola.model.InstructorDocuments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstructorDocumentsRepository extends JpaRepository<InstructorDocuments, Long> {
}
