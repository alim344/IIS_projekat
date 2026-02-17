package com.example.autoskola.config;

import com.example.autoskola.model.*;
import com.example.autoskola.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
public class TestDataLoader implements CommandLineRunner {
    private final UserRepository userRepository;
    private final CandidateRepository candidateRepository;
    private final InstructorRepository instructorRepository;
    private final RoleRepository roleRepository;
    private final ProfessorRepository professorRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        if (userRepository.count() > 0) return; // prevents duplicating on restart

        // roles
        Role candidateRole = roleRepository.save(new Role("ROLE_CANDIDATE"));
        Role instructorRole = roleRepository.save(new Role("ROLE_INSTRUCTOR"));
        Role professorRole = roleRepository.save(new Role("ROLE_PROFESSOR"));
        Role adminRole = roleRepository.save(new Role("ROLE_ADMIN"));

        Admin admin = new Admin();
        admin.setEmail("admin@gmail.com");
        admin.setName("Admin");
        admin.setLastname("Adminic");
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("123"));
        admin.setEnabled(true);
        admin.setRole(adminRole);
        adminRepository.save(admin);

        // ----- PROFESSOR -----
        Professor professor = new Professor();
        professor.setEmail("prof@gmail.com");
        professor.setName("Petar");
        professor.setLastname("Petrovic");
        professor.setUsername("prof");
        professor.setPassword(passwordEncoder.encode("123"));
        professor.setEnabled(true);
        professor.setRole(professorRole);
        professorRepository.save(professor);


        // instructor
        Instructor instructor = new Instructor();
        instructor.setEmail("inst@gmail.com");
        instructor.setName("Marko");
        instructor.setLastname("Markovic");
        instructor.setUsername("marko");
        instructor.setPassword(passwordEncoder.encode("123"));
        instructor.setEnabled(true);
        instructor.setRole(instructorRole);

        instructorRepository.save(instructor);

        // candidate
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
        candidate.setPreferredLocation("Center");

        candidateRepository.save(candidate);

        System.out.println("TEST DATA LOADED");
    }
}
