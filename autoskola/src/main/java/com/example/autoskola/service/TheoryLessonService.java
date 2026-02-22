package com.example.autoskola.service;

import com.example.autoskola.model.TheoryLesson;
import com.example.autoskola.repository.TheoryLessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TheoryLessonService {

    @Autowired
    private TheoryLessonRepository theoryLessonRepository;

    public List<TheoryLesson> findAllByOrderByOrderNumberAsc(){
        return theoryLessonRepository.findAllByOrderByOrderNumberAsc();
    }
}
