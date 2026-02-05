package com.example.autoskola.dto;

import com.example.autoskola.model.DocumentType;
import com.example.autoskola.model.InstructorDocuments;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InstructorDocumentsDTO {

    private DocumentType documentType;

    private LocalDate expiryDate;

    public InstructorDocumentsDTO(InstructorDocuments doc) {
        this.documentType = doc.getDocumentType();
        this.expiryDate = doc.getExpiryDate();
    }
}
