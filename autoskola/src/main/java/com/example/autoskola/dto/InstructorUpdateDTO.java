package com.example.autoskola.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class InstructorUpdateDTO {
    private String name;

    private String lastname;

    private Long vehicleId;

    private List<InstructorDocumentsDTO> documents;

    public InstructorUpdateDTO(String name, String lastname, Long vehicleId, List<InstructorDocumentsDTO> documents) {
        this.name = name;
        this.lastname = lastname;
        this.vehicleId = vehicleId;
        this.documents = documents;
    }
}
