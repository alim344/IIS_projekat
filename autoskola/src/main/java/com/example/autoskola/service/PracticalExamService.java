package com.example.autoskola.service;

import com.example.autoskola.repository.PracticalExamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PracticalExamService {

    @Autowired
    private PracticalExamRepository practicalExamRepository;
}
