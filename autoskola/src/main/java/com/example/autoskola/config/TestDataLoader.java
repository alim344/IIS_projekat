package com.example.autoskola.config;

import com.example.autoskola.model.*;
import com.example.autoskola.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Configuration
@RequiredArgsConstructor
public class TestDataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CandidateRepository candidateRepository;
    private final InstructorRepository instructorRepository;
    private final ProfessorRepository professorRepository;
    private final AdminRepository adminRepository;
    private final RoleRepository roleRepository;
    private final VehicleRepository vehicleRepository;
    private final TimePreferenceRepository timePreferenceRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        // prevents reseeding after restart
        if (userRepository.count() > 0) return;

        // ---------------- ROLES ----------------
        Role candidateRole = roleRepository.save(new Role("ROLE_CANDIDATE"));
        Role instructorRole = roleRepository.save(new Role("ROLE_INSTRUCTOR"));
        Role professorRole = roleRepository.save(new Role("ROLE_PROFESSOR"));
        Role adminRole = roleRepository.save(new Role("ROLE_ADMIN"));

        // ---------------- VEHICLES ----------------
        Vehicle vehicle1 = new Vehicle();
        vehicle1.setRegistrationNumber("NS-123-AA");
        vehicle1.setRegistrationExpiryDate(LocalDate.now().plusMonths(6));
        vehicle1.setStatus(VehicleStatus.AVAILABLE);
        vehicle1.setCurrentMileage(45200);
        vehicleRepository.save(vehicle1);

        Vehicle vehicle2 = new Vehicle();
        vehicle2.setRegistrationNumber("SU-456-BB");
        vehicle2.setRegistrationExpiryDate(LocalDate.now().plusMonths(3));
        vehicle2.setStatus(VehicleStatus.IN_USE);
        vehicle2.setCurrentMileage(81200);
        vehicleRepository.save(vehicle2);

        // ---------------- ADMIN ----------------
        Admin admin = new Admin();
        admin.setEmail("admin@gmail.com");
        admin.setName("Admin");
        admin.setLastname("Adminic");
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("123"));
        admin.setEnabled(true);
        admin.setRole(adminRole);
        adminRepository.save(admin);

        // ---------------- PROFESSOR ----------------
        Professor professor = new Professor();
        professor.setEmail("prof@gmail.com");
        professor.setName("Petar");
        professor.setLastname("Petrovic");
        professor.setUsername("prof");
        professor.setPassword(passwordEncoder.encode("123"));
        professor.setEnabled(true);
        professor.setRole(professorRole);
        professorRepository.save(professor);

        // ---------------- INSTRUCTOR ----------------
        Instructor instructor = new Instructor();
        instructor.setEmail("inst@gmail.com");
        instructor.setName("Marko");
        instructor.setLastname("Markovic");
        instructor.setUsername("marko");
        instructor.setPassword(passwordEncoder.encode("123"));
        instructor.setEnabled(true);
        instructor.setRole(instructorRole);
        instructorRepository.save(instructor);

        // connect instructor ↔ vehicle
        instructor.setVehicle(vehicle1);
        vehicle1.setInstructor(instructor);
        vehicleRepository.save(vehicle1);

        // ---------------- CANDIDATE ----------------
        Candidate candidate = new Candidate();
        candidate.setEmail("ana@gmail.com");
        candidate.setName("Ana");
        candidate.setLastname("Anic");
        candidate.setUsername("ana");
        candidate.setPassword(passwordEncoder.encode("123"));
        candidate.setEnabled(true);
        candidate.setRole(candidateRole);

        candidate.setStartOfTraining(LocalDateTime.now());
        candidate.setCategory(Category.B);
        candidate.setStatus(TrainingStatus.THEORY);
        candidate.setInstructor(instructor);
        candidate.setPreferredLocation("City Center");

        candidateRepository.save(candidate);

        // ---------------- TIME PREFERENCE ----------------
        TimePreference pref = new TimePreference();
        pref.setCandidate(candidate);
        pref.setDate(LocalDate.now().plusDays(1));
        pref.setStartTime(LocalTime.of(9,0));
        pref.setEndTime(LocalTime.of(13,0));
        timePreferenceRepository.save(pref);

        System.out.println("TEST DATA LOADED SUCCESSFULLY");
    }
}

