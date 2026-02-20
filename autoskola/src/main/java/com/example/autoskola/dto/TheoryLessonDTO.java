package com.example.autoskola.dto;

import com.example.autoskola.model.TheoryLesson;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TheoryLessonDTO {
    private Long id;
    private String name;
    private Integer orderNumber;

    public TheoryLessonDTO() {}

    public TheoryLessonDTO(TheoryLesson lesson) {
        this.id = lesson.getId();
        this.name = lesson.getName();
        this.orderNumber = lesson.getOrderNumber();
    }
}
