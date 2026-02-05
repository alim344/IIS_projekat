package com.example.autoskola.controller;

import com.example.autoskola.dto.InstructorDTO;
import com.example.autoskola.dto.InstructorUpdateDTO;
import com.example.autoskola.dto.VehicleDTO;
import com.example.autoskola.model.Instructor;
import com.example.autoskola.model.User;
import com.example.autoskola.service.InstructorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/instructors")
@RequiredArgsConstructor
public class InstructorController {
    @Autowired
    private InstructorService instructorService;

    @GetMapping("/me")
    public ResponseEntity<InstructorDTO> getMyProfile(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        Instructor instructor = instructorService.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Instructor not found."));

        return ResponseEntity.ok(new InstructorDTO(instructor));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<InstructorDTO>> getAllInstructors() {
        List<Instructor> instructors = instructorService.findAll();

        List<InstructorDTO> dtos = instructors.stream()
                .map(i -> new InstructorDTO(i))
                .toList();

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<InstructorDTO> getInstructorById(@PathVariable Long id) {
        Instructor instructor = instructorService.findById(id)
                .orElseThrow(() -> new RuntimeException("Instructor not found."));

        return ResponseEntity.ok(new InstructorDTO(instructor));
    }

    @PutMapping("/{instructorId}/vehicle/{vehicleId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> assignVehicle(
            @PathVariable Long instructorId,
            @PathVariable Long vehicleId) {

        instructorService.assignVehicleToInstructor(instructorId, vehicleId);
        return ResponseEntity.ok("Vehicle assigned successfully.");
    }

   @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<InstructorDTO> updateInstructor(@PathVariable Long id, @RequestBody InstructorUpdateDTO instructorDto) {
       Instructor updatedInstructor = instructorService.update(id, instructorDto);

       InstructorDTO response = new InstructorDTO(updatedInstructor);

       return ResponseEntity.ok(response);
   }

}
