package com.example.autoskola.service;

import com.example.autoskola.repository.TheoryClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TheoryClassService {

    @Autowired
    private TheoryClassRepository theoryClassRepository;
}
