package com.example.autoskola.repository;


import com.example.autoskola.model.Category;
import com.example.autoskola.model.TheoryLesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TheoryLessonRepository extends JpaRepository<TheoryLesson, Long> {
    List<TheoryLesson> findAllByOrderByOrderNumberAsc();

}
