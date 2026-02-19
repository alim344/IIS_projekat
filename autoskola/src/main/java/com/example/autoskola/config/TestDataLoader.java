package com.example.autoskola.config;

import com.example.autoskola.model.*;
import com.example.autoskola.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.load-test-data", havingValue = "true")
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
    private final PracticalClassRepository practicalClassRepository;

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
        instructor.setMaxCapacity(5);
        instructorRepository.save(instructor);

        // connect instructor ↔ vehicle
        instructor.setVehicle(vehicle1);
        vehicle1.setInstructor(instructor);
        vehicleRepository.save(vehicle1);

        instructorRepository.save(instructor);
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

        // ---------------- PRACTICAL CLASSES ----------------
        PracticalClass class1 = new PracticalClass();
        class1.setCandidate(candidate);
        class1.setInstructor(instructor);
        class1.setStartTime(LocalDateTime.now().plusDays(1).withHour(9).withMinute(0));
        class1.setEndTime(LocalDateTime.now().plusDays(1).withHour(10).withMinute(30));
        class1.setAccepted(false);
        practicalClassRepository.save(class1);

        PracticalClass class2 = new PracticalClass();
        class2.setCandidate(candidate);
        class2.setInstructor(instructor);
        class2.setStartTime(LocalDateTime.now().plusDays(2).withHour(11).withMinute(0));
        class2.setEndTime(LocalDateTime.now().plusDays(2).withHour(12).withMinute(30));
        class2.setAccepted(true);
        practicalClassRepository.save(class2);

        //cas koji je prosao
        PracticalClass class3 = new PracticalClass();
        class3.setCandidate(candidate);
        class3.setInstructor(instructor);
        class3.setStartTime(LocalDateTime.now().minusDays(2).withHour(12).withMinute(30));
        class3.setEndTime(LocalDateTime.now().minusDays(2).withHour(15).withMinute(30));
        class3.setAccepted(true);
        practicalClassRepository.save(class3);

        //cas koji je prosao
        PracticalClass class4 = new PracticalClass();
        class4.setCandidate(candidate);
        class4.setInstructor(instructor);
        class4.setStartTime(LocalDateTime.now().minusDays(1).withHour(12).withMinute(30));
        class4.setEndTime(LocalDateTime.now().minusDays(1).withHour(15).withMinute(30));
        class4.setAccepted(true);
        practicalClassRepository.save(class4);

        System.out.println("TEST DATA LOADED SUCCESSFULLY");
    }
}

