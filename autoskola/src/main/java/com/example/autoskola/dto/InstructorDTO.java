package com.example.autoskola.dto;

import com.example.autoskola.model.Instructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InstructorDTO {

    private Long id;

    private String username;

    private String email;

    private String name;

    private String lastName;

    private VehicleDTO vehicle;

    private List<InstructorDocumentsDTO> documents;

    public InstructorDTO(Instructor instructor) {
        this.id = instructor.getId();
        this.username = instructor.getUsername();
        this.email = instructor.getEmail();
        this.name = instructor.getName();
        this.lastName = instructor.getLastname();

        if (instructor.getVehicle() != null) {
            this.vehicle = new VehicleDTO(instructor.getVehicle());
        }

        this.documents = instructor.getDocuments()
                .stream()
                .map(doc -> new InstructorDocumentsDTO(doc))
                .toList();
    }

}
