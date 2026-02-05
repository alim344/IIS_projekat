package com.example.autoskola.repository;

import com.example.autoskola.model.Instructor;
import com.example.autoskola.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstructorRepository extends JpaRepository<Instructor, Long> {
    Instructor save(Instructor instructor);

    Instructor findByEmail(String email);

}
