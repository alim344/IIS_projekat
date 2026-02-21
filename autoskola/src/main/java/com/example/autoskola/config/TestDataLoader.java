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
    private final TheoryLessonRepository theoryLessonRepository;
    private final TheoryClassRepository theoryClassRepository;

    @Override
    public void run(String... args) {

        if (userRepository.count() > 0) return;

        // ---------------- ROLES ----------------
        Role candidateRole = roleRepository.save(new Role("ROLE_CANDIDATE"));
        Role instructorRole = roleRepository.save(new Role("ROLE_INSTRUCTOR"));
        Role professorRole = roleRepository.save(new Role("ROLE_PROFESSOR"));
        Role adminRole = roleRepository.save(new Role("ROLE_ADMIN"));

        // ---------------- THEORY LESSONS (40) ----------------
        String[][] lessons = {
                {"Uvod u saobraćajne propise", "1"},
                {"Osnovna pravila saobraćaja", "2"},
                {"Saobraćajni znaci - opšte", "3"},
                {"Znaci opasnosti", "4"},
                {"Znaci izričitih naredbi", "5"},
                {"Znaci obaveštenja", "6"},
                {"Dopunske table", "7"},
                {"Svetlosna saobraćajna signalizacija", "8"},
                {"Oznake na kolovozu", "9"},
                {"Ovlašćena lica za regulisanje saobraćaja", "10"},
                {"Pravo prolaza na raskrsnici", "11"},
                {"Pravo prolaza na raskrsnici - posebni slučajevi", "12"},
                {"Brzina kretanja vozila", "13"},
                {"Razmak između vozila", "14"},
                {"Obilaženje i preticanje", "15"},
                {"Mimoilaženje vozila", "16"},
                {"Prolaženje pored zaustavljenog vozila", "17"},
                {"Skretanje vozila", "18"},
                {"Kretanje vozila unazad", "19"},
                {"Kretanje vozila u kružnom toku", "20"},
                {"Zaustavljanje i parkiranje", "21"},
                {"Zaustavljanje i parkiranje - zabrane", "22"},
                {"Upotreba svetala", "23"},
                {"Upotreba zvučnih i svetlosnih znakova", "24"},
                {"Teret na vozilu", "25"},
                {"Vučenje neispravnog vozila", "26"},
                {"Pešaci u saobraćaju", "27"},
                {"Biciklisti u saobraćaju", "28"},
                {"Motociklisti u saobraćaju", "29"},
                {"Posebni učesnici u saobraćaju", "30"},
                {"Saobraćaj na auto-putu", "31"},
                {"Saobraćaj u tunelima", "32"},
                {"Saobraćaj po lošim vremenskim uslovima", "33"},
                {"Tehničke karakteristike vozila", "34"},
                {"Aktivna i pasivna bezbednost vozila", "35"},
                {"Upravljanje vozilom - osnove", "36"},
                {"Opasnost od alkohola i droga u saobraćaju", "37"},
                {"Zamor i bolesti vozača", "38"},
                {"Prva pomoć u saobraćaju", "39"},
                {"Ekološka vožnja i ekonomičnost", "40"}
        };

        for (String[] lesson : lessons) {
            TheoryLesson tl = new TheoryLesson();
            tl.setName(lesson[0]);
            tl.setOrderNumber(Integer.parseInt(lesson[1]));
            theoryLessonRepository.save(tl);
        }

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

        // ---------------- PROFESSORS ----------------
        Professor professor1 = new Professor();
        professor1.setEmail("prof@gmail.com");
        professor1.setName("Petar");
        professor1.setLastname("Petrovic");
        professor1.setUsername("prof");
        professor1.setPassword(passwordEncoder.encode("123"));
        professor1.setEnabled(true);
        professor1.setRole(professorRole);
        professorRepository.save(professor1);

        Professor professor2 = new Professor();
        professor2.setEmail("prof2@gmail.com");
        professor2.setName("Jovana");
        professor2.setLastname("Jovanovic");
        professor2.setUsername("prof2");
        professor2.setPassword(passwordEncoder.encode("123"));
        professor2.setEnabled(true);
        professor2.setRole(professorRole);
        professorRepository.save(professor2);

        // ---------------- INSTRUCTORS ----------------
        Instructor instructor1 = new Instructor();
        instructor1.setEmail("inst@gmail.com");
        instructor1.setName("Marko");
        instructor1.setLastname("Markovic");
        instructor1.setUsername("marko");
        instructor1.setPassword(passwordEncoder.encode("123"));
        instructor1.setEnabled(true);
        instructor1.setRole(instructorRole);
        instructor1.setMaxCapacity(10);
        instructorRepository.save(instructor1);

        Instructor instructor2 = new Instructor();
        instructor2.setEmail("inst2@gmail.com");
        instructor2.setName("Nikola");
        instructor2.setLastname("Nikolic");
        instructor2.setUsername("nikola");
        instructor2.setPassword(passwordEncoder.encode("123"));
        instructor2.setEnabled(true);
        instructor2.setRole(instructorRole);
        instructor2.setMaxCapacity(8);
        instructorRepository.save(instructor2);

        instructor1.setVehicle(vehicle1);
        vehicle1.setInstructor(instructor1);
        vehicleRepository.save(vehicle1);
        instructorRepository.save(instructor1);

        instructor2.setVehicle(vehicle2);
        vehicle2.setInstructor(instructor2);
        vehicleRepository.save(vehicle2);
        instructorRepository.save(instructor2);

        // ---------------- CANDIDATES WITH PREFERENCE ----------------
        // MORNING (08:00-09:00) - preferenca 07:00-12:00
        Candidate c1 = makeCandidate("ana@gmail.com", "Ana", "Anic", "ana", Category.B, instructor1, candidateRole);
        candidateRepository.save(c1);
        savePreference(c1, LocalTime.of(7, 0), LocalTime.of(12, 0));

        Candidate c2 = makeCandidate("milan@gmail.com", "Milan", "Milanovic", "milan", Category.B, instructor1, candidateRole);
        candidateRepository.save(c2);
        savePreference(c2, LocalTime.of(7, 0), LocalTime.of(12, 0));

        Candidate c3 = makeCandidate("maja@gmail.com", "Maja", "Majic", "maja", Category.B, instructor1, candidateRole);
        candidateRepository.save(c3);
        savePreference(c3, LocalTime.of(7, 0), LocalTime.of(12, 0));

        Candidate c4 = makeCandidate("ana1@gmail.com", "Anica", "Anic", "anica", Category.B, instructor1, candidateRole);
        candidateRepository.save(c4);
        savePreference(c4, LocalTime.of(7, 0), LocalTime.of(12, 0));

        Candidate c5 = makeCandidate("miljan@gmail.com", "Miljan", "Milanovic", "miljan", Category.B, instructor1, candidateRole);
        candidateRepository.save(c5);
        savePreference(c5, LocalTime.of(7, 0), LocalTime.of(12, 0));

        Candidate c6 = makeCandidate("majamajic@gmail.com", "Maja", "Majic", "majamajic", Category.B, instructor1, candidateRole);
        candidateRepository.save(c6);
        savePreference(c6, LocalTime.of(7, 0), LocalTime.of(12, 0));

        // MORNING (08:00-10:00) - preferenca 07:00-12:00
        Candidate cc7 = makeCandidate("marko1@gmail.com", "Marko", "Markovic", "marko1", Category.B, instructor1, candidateRole);
        candidateRepository.save(cc7);
        savePreference(cc7, LocalTime.of(7, 0), LocalTime.of(12, 0));

        Candidate cc8 = makeCandidate("sara1@gmail.com", "Sara", "Saric", "sara1", Category.B, instructor1, candidateRole);
        candidateRepository.save(cc8);
        savePreference(cc8, LocalTime.of(7, 0), LocalTime.of(12, 0));

        Candidate cc9 = makeCandidate("nikola1@gmail.com", "Nikola", "Nikolic", "nikola1", Category.B, instructor1, candidateRole);
        candidateRepository.save(cc9);
        savePreference(cc9, LocalTime.of(7, 0), LocalTime.of(12, 0));

        Candidate cc10 = makeCandidate("jelena1@gmail.com", "Jelena", "Jelic", "jelena1", Category.B, instructor1, candidateRole);
        candidateRepository.save(cc10);
        savePreference(cc10, LocalTime.of(7, 0), LocalTime.of(12, 0));

        Candidate cc11 = makeCandidate("stefan1@gmail.com", "Stefan", "Stefanovic", "stefan1", Category.B, instructor1, candidateRole);
        candidateRepository.save(cc11);
        savePreference(cc11, LocalTime.of(7, 0), LocalTime.of(12, 0));

        Candidate cc12 = makeCandidate("ana21@gmail.com", "Ana", "Petrovic", "ana21", Category.B, instructor1, candidateRole);
        candidateRepository.save(cc12);
        savePreference(cc12, LocalTime.of(7, 0), LocalTime.of(12, 0));

        Candidate cc13 = makeCandidate("david1@gmail.com", "David", "Davidovic", "david1", Category.B, instructor1, candidateRole);
        candidateRepository.save(cc13);
        savePreference(cc13, LocalTime.of(7, 0), LocalTime.of(12, 0));

        Candidate cc14 = makeCandidate("luna1@gmail.com", "Luna", "Lunic", "luna1", Category.B, instructor1, candidateRole);
        candidateRepository.save(cc14);
        savePreference(cc14, LocalTime.of(7, 0), LocalTime.of(12, 0));

        Candidate cc15 = makeCandidate("lazar1@gmail.com", "Lazar", "Lazarevic", "lazar1", Category.B, instructor1, candidateRole);
        candidateRepository.save(cc15);
        savePreference(cc15, LocalTime.of(7, 0), LocalTime.of(12, 0));

        Candidate cc16 = makeCandidate("mina1@gmail.com", "Mina", "Minic", "mina1", Category.B, instructor1, candidateRole);
        candidateRepository.save(cc16);
        savePreference(cc16, LocalTime.of(7, 0), LocalTime.of(12, 0));

        Candidate cc17 = makeCandidate("dusan1@gmail.com", "Dusan", "Dusanic", "dusan1", Category.B, instructor1, candidateRole);
        candidateRepository.save(cc17);
        savePreference(cc17, LocalTime.of(7, 0), LocalTime.of(12, 0));

        Candidate cc18 = makeCandidate("jovana@gmail.com", "Jovana", "Jovanovic", "jovana", Category.B, instructor1, candidateRole);
        candidateRepository.save(cc18);
        savePreference(cc18, LocalTime.of(7, 0), LocalTime.of(12, 0));

        Candidate cc19 = makeCandidate("Filip2@gmail.com", "Filip", "Filipovic", "filip2", Category.B, instructor1, candidateRole);
        candidateRepository.save(cc19);
        savePreference(cc19, LocalTime.of(7, 0), LocalTime.of(12, 0));

        Candidate cc20 = makeCandidate("teodora2@gmail.com", "Teodora", "Teodorovic", "teodora2", Category.B, instructor1, candidateRole);
        candidateRepository.save(cc20);
        savePreference(cc20, LocalTime.of(7, 0), LocalTime.of(12, 0));

        Candidate cc21 = makeCandidate("petar2@gmail.com", "Petar", "Petrovic", "petar2", Category.B, instructor1, candidateRole);
        candidateRepository.save(cc21);
        savePreference(cc21, LocalTime.of(7, 0), LocalTime.of(12, 0));

        Candidate cc22 = makeCandidate("katarina2@gmail.com", "Katarina", "Katanic", "katarina2", Category.B, instructor1, candidateRole);
        candidateRepository.save(cc22);
        savePreference(cc22, LocalTime.of(7, 0), LocalTime.of(12, 0));

        Candidate cc23 = makeCandidate("uros2@gmail.com", "Uros", "Urosevic", "uros2", Category.B, instructor1, candidateRole);
        candidateRepository.save(cc23);
        savePreference(cc23, LocalTime.of(7, 0), LocalTime.of(12, 0));

        Candidate cc24 = makeCandidate("sofija2@gmail.com", "Sofija", "Sofic", "sofija2", Category.B, instructor1, candidateRole);
        candidateRepository.save(cc24);
        savePreference(cc24, LocalTime.of(7, 0), LocalTime.of(12, 0));

        Candidate cc25 = makeCandidate("vuk2@gmail.com", "Vuk", "Vukovic", "vuk2", Category.B, instructor1, candidateRole);
        candidateRepository.save(cc25);
        savePreference(cc25, LocalTime.of(7, 0), LocalTime.of(12, 0));

        Candidate cc26 = makeCandidate("emilija2@gmail.com", "Emilija", "Emilic", "emilija2", Category.B, instructor1, candidateRole);
        candidateRepository.save(cc26);
        savePreference(cc26, LocalTime.of(7, 0), LocalTime.of(12, 0));

        // AFTERNOON (14:00-15:00) - preferenca 13:00-17:00
        Candidate c17 = makeCandidate("stefan2@gmail.com", "Stefan", "Stefanovic", "stefan2", Category.B, instructor2, candidateRole);
        candidateRepository.save(c17);
        savePreference(c17, LocalTime.of(13, 0), LocalTime.of(17, 0));

        Candidate c18 = makeCandidate("veljko2@gmail.com", "Veljko", "Jelenic", "veljko2", Category.B, instructor2, candidateRole);
        candidateRepository.save(c18);
        savePreference(c18, LocalTime.of(13, 0), LocalTime.of(17, 0));

        Candidate c19 = makeCandidate("milosiv@gmail.com", "Milos", "Ivanovic", "ivan", Category.B, instructor2, candidateRole);
        candidateRepository.save(c19);
        savePreference(c19, LocalTime.of(13, 0), LocalTime.of(17, 0));

        // EVENING (18:00-19:00) - preferenca 17:00-20:00
        Candidate c7 = makeCandidate("marija@gmail.com", "Marija", "Maric", "marija", Category.B, instructor1, candidateRole);
        candidateRepository.save(c7);
        savePreference(c7, LocalTime.of(17, 0), LocalTime.of(20, 0));

        Candidate c8 = makeCandidate("petar@gmail.com", "Petar", "Petric", "petar", Category.B, instructor1, candidateRole);
        candidateRepository.save(c8);
        savePreference(c8, LocalTime.of(17, 0), LocalTime.of(20, 0));

        // Široka preferenca - pokriva sve slotove
        Candidate c9 = makeCandidate("sanja@gmail.com", "Sanja", "Sanjic", "sanja", Category.B, instructor2, candidateRole);
        candidateRepository.save(c9);
        savePreference(c9, LocalTime.of(7, 0), LocalTime.of(22, 0));

        Candidate c10 = makeCandidate("luka@gmail.com", "Luka", "Lukic", "luka", Category.B, instructor2, candidateRole);
        candidateRepository.save(c10);
        savePreference(c10, LocalTime.of(7, 0), LocalTime.of(22, 0));

        // Kategorija A sa preferencama
        Candidate c11 = makeCandidate("aleksandar@gmail.com", "Aleksandar", "Aleksic", "aleksa", Category.A, instructor1, candidateRole);
        candidateRepository.save(c11);
        savePreference(c11, LocalTime.of(7, 0), LocalTime.of(12, 0));

        Candidate c12 = makeCandidate("nina@gmail.com", "Nina", "Ninic", "nina", Category.A, instructor1, candidateRole);
        candidateRepository.save(c12);
        savePreference(c12, LocalTime.of(13, 0), LocalTime.of(17, 0));

        // ---------------- CANDIDATES WITHOUT PREFERENCE ----------------
        Candidate c13 = makeCandidate("bojan@gmail.com", "Bojan", "Bojanic", "bojan", Category.B, instructor1, candidateRole);
        candidateRepository.save(c13);

        Candidate c14 = makeCandidate("dragana@gmail.com", "Dragana", "Draganic", "dragana", Category.B, instructor2, candidateRole);
        candidateRepository.save(c14);

        Candidate c15 = makeCandidate("milos@gmail.com", "Milos", "Milosevic", "milos", Category.B, instructor1, candidateRole);
        candidateRepository.save(c15);

        Candidate c16 = makeCandidate("tamara@gmail.com", "Tamara", "Tamaric", "tamara", Category.A, instructor2, candidateRole);
        candidateRepository.save(c16);

        // ---------------- PRACTICAL CLASSES ----------------
        savePracticalClass(c1, instructor1, LocalDateTime.now().plusDays(1).withHour(9).withMinute(0), LocalDateTime.now().plusDays(1).withHour(10).withMinute(30), false);
        savePracticalClass(c1, instructor1, LocalDateTime.now().plusDays(2).withHour(11).withMinute(0), LocalDateTime.now().plusDays(2).withHour(12).withMinute(30), true);
        savePracticalClass(c1, instructor1, LocalDateTime.now().minusDays(2).withHour(12).withMinute(30), LocalDateTime.now().minusDays(2).withHour(15).withMinute(30), true);
        savePracticalClass(c4, instructor2, LocalDateTime.now().plusDays(1).withHour(14).withMinute(0), LocalDateTime.now().plusDays(1).withHour(15).withMinute(30), false);
        savePracticalClass(c7, instructor1, LocalDateTime.now().plusDays(3).withHour(18).withMinute(0), LocalDateTime.now().plusDays(3).withHour(19).withMinute(30), false);

//        // ---------------- THEORY CLASSES ----------------
//        // Današnji čas (sada)
//        TheoryClass theoryClass1 = new TheoryClass();
//        theoryClass1.setProfessor(professor1);
//        theoryClass1.setStartTime(LocalDateTime.now());
//        theoryClass1.setEndTime(LocalDateTime.now().plusHours(1));
//        theoryClass1.setCapacity(20);
//        theoryClass1.setEnrolledStudents(0);
//        theoryClass1.setTheoryLesson(theoryLessonRepository.findAll().get(0));
//        theoryClassRepository.save(theoryClass1);
//
//        // Sutrašnji čas
//        TheoryClass theoryClass2 = new TheoryClass();
//        theoryClass2.setProfessor(professor1);
//        theoryClass2.setStartTime(LocalDateTime.now().plusDays(1).withHour(10).withMinute(0));
//        theoryClass2.setEndTime(LocalDateTime.now().plusDays(1).withHour(12).withMinute(0));
//        theoryClass2.setCapacity(15);
//        theoryClass2.setEnrolledStudents(0);
//        theoryClass2.setTheoryLesson(theoryLessonRepository.findAll().get(1));
//        theoryClassRepository.save(theoryClass2);
//
//        // Prekjučerašnji čas (završen)
//        TheoryClass theoryClass3 = new TheoryClass();
//        theoryClass3.setProfessor(professor2);
//        theoryClass3.setStartTime(LocalDateTime.now().minusDays(2).withHour(17).withMinute(0));
//        theoryClass3.setEndTime(LocalDateTime.now().minusDays(2).withHour(19).withMinute(0));
//        theoryClass3.setCapacity(25);
//        theoryClass3.setEnrolledStudents(3);
//        theoryClass3.setTheoryLesson(theoryLessonRepository.findAll().get(5)); // Šesta lekcija
//        // Dodaj par kandidata koji su prisustvovali
//        theoryClass3.getStudents().add(c1);
//        theoryClass3.getStudents().add(c2);
//        theoryClass3.getStudents().add(c3);
//        theoryClassRepository.save(theoryClass3);


        System.out.println("TEST DATA LOADED SUCCESSFULLY");
    }

    private Candidate makeCandidate(String email, String name, String lastname, String username,
                                    Category category, Instructor instructor, Role role) {
        Candidate c = new Candidate();
        c.setEmail(email);
        c.setName(name);
        c.setLastname(lastname);
        c.setUsername(username);
        c.setPassword(passwordEncoder.encode("123"));
        c.setEnabled(true);
        c.setRole(role);
        c.setStartOfTraining(LocalDateTime.now());
        c.setCategory(category);
        c.setStatus(TrainingStatus.THEORY);
        c.setInstructor(instructor);
        c.setPreferredLocation("Novi Sad");
        return c;
    }

    private void savePreference(Candidate candidate, LocalTime start, LocalTime end) {
        TimePreference pref = new TimePreference();
        pref.setCandidate(candidate);
        pref.setStartTime(start);
        pref.setEndTime(end);
        timePreferenceRepository.save(pref);
    }

    private void savePracticalClass(Candidate candidate, Instructor instructor,
                                    LocalDateTime start, LocalDateTime end, boolean accepted) {
        PracticalClass pc = new PracticalClass();
        pc.setCandidate(candidate);
        pc.setInstructor(instructor);
        pc.setStartTime(start);
        pc.setEndTime(end);
        pc.setAccepted(accepted);
        practicalClassRepository.save(pc);
    }
}
