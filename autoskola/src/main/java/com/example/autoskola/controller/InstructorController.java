package com.example.autoskola.controller;

import com.example.autoskola.dto.InstructorAnalyticsDTO;
import com.example.autoskola.dto.InstructorDTO;
import com.example.autoskola.dto.InstructorUpdateDTO;
import com.example.autoskola.dto.VehicleDTO;
import com.example.autoskola.model.Instructor;
import com.example.autoskola.model.User;
import com.example.autoskola.service.InstructorAnalytics;
import com.example.autoskola.service.InstructorService;
import com.example.autoskola.util.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
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
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private InstructorAnalytics instructorAnalytics;

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

   @PutMapping("/{id}/remove-vehicle")
   @PreAuthorize("hasRole('ROLE_ADMIN')")
   public ResponseEntity<?> removeVehicle(@PathVariable Long id) {
        instructorService.removeVehicle(id);
       return ResponseEntity.ok("Vehicle removed successfully.");
   }

    @GetMapping("/my-vehicle")
    @PreAuthorize("hasRole('ROLE_INSTRUCTOR')")
    public ResponseEntity<VehicleDTO> getMyVehicle(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        Instructor instructor = instructorService.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Instructor not found."));

        if (instructor.getVehicle() == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(new VehicleDTO(instructor.getVehicle()));
    }


    @GetMapping("/getAnalytics")
    public ResponseEntity<InstructorAnalyticsDTO> getExamAnalytics(HttpServletRequest request) {
        String token = tokenUtils.getToken(request);
        String email = tokenUtils.getEmailFromToken(token);
        Instructor instructor = instructorService.findByEmail(email);

        return ResponseEntity.ok(instructorAnalytics.getAnalytics(instructor.getId()));
    }

}
