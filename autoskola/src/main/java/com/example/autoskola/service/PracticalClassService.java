package com.example.autoskola.service;

import com.example.autoskola.repository.PracticalClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PracticalClassService {

    @Autowired
    private PracticalClassRepository practicalClassRepository;

}
