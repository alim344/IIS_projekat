package com.example.autoskola.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class InstructorDocuments {

    @Id
    private Long id;

    private DocumentType documentType;

    private LocalDateTime expiryDate;

    @ManyToOne
    @JoinColumn(name = "instructor_id")
    private Instructor instructor;

}
