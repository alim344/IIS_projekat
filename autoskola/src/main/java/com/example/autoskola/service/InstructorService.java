package com.example.autoskola.service;

import com.example.autoskola.model.Instructor;
import com.example.autoskola.repository.InstructorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InstructorService {

    @Autowired
    private InstructorRepository instructorRepository;

    public Optional<Instructor> findById(Long id) {
        return instructorRepository.findById(id);
    }

    public List<Instructor> findAll() { return instructorRepository.findAll(); }
}
