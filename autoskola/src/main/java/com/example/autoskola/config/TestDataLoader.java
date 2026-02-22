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
import java.util.List;

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
    private final TheoryExamRepository theoryExamRepository;

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

        // AFTERNOON (14:00-15:00) - preferenca 13:00-17:00
        Candidate c4 = makeCandidate("stefan@gmail.com", "Stefan", "Stefanovic", "stefan", Category.B, instructor2, candidateRole);
        candidateRepository.save(c4);
        savePreference(c4, LocalTime.of(13, 0), LocalTime.of(17, 0));

        Candidate c5 = makeCandidate("jelena@gmail.com", "Jelena", "Jelenic", "jelena", Category.B, instructor2, candidateRole);
        candidateRepository.save(c5);
        savePreference(c5, LocalTime.of(13, 0), LocalTime.of(17, 0));

        Candidate c6 = makeCandidate("ivan@gmail.com", "Ivan", "Ivanovic", "ivan", Category.B, instructor2, candidateRole);
        candidateRepository.save(c6);
        savePreference(c6, LocalTime.of(13, 0), LocalTime.of(17, 0));

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

        // ---------------- PENDING CANDIDATES (završili teoriju, svih 40 lekcija) ----------------
        List<TheoryLesson> allLessons = theoryLessonRepository.findAll();

        Candidate p1 = makePendingCandidate("zoran@gmail.com", "Zoran", "Zoranic", "zoran", Category.B, instructor1, candidateRole);
        p1.getAttendedLessons().addAll(allLessons);
        candidateRepository.save(p1);

        Candidate p2 = makePendingCandidate("vesna@gmail.com", "Vesna", "Vesnic", "vesna", Category.B, instructor2, candidateRole);
        p2.getAttendedLessons().addAll(allLessons);
        candidateRepository.save(p2);

        Candidate p3 = makePendingCandidate("dragan@gmail.com", "Dragan", "Draganovic", "dragan", Category.A, instructor1, candidateRole);
        p3.getAttendedLessons().addAll(allLessons);
        candidateRepository.save(p3);

        Candidate p4 = makePendingCandidate("ivana@gmail.com", "Ivana", "Ivanic", "ivana", Category.B, instructor2, candidateRole);
        p4.getAttendedLessons().addAll(allLessons);
        candidateRepository.save(p4);

        Candidate p5 = makePendingCandidate("nemanja@gmail.com", "Nemanja", "Nemanjic", "nemanja", Category.A, instructor1, candidateRole);
        p5.getAttendedLessons().addAll(allLessons);
        candidateRepository.save(p5);

        Candidate p6 = makePendingCandidate("gordana@gmail.com", "Gordana", "Gordanic", "gordana", Category.B, instructor1, candidateRole);
        p6.getAttendedLessons().addAll(allLessons);
        candidateRepository.save(p6);

        Candidate p7 = makePendingCandidate("slobodan@gmail.com", "Slobodan", "Slobodanovic", "slobodan", Category.B, instructor2, candidateRole);
        p7.getAttendedLessons().addAll(allLessons);
        candidateRepository.save(p7);

        Candidate p8 = makePendingCandidate("milica@gmail.com", "Milica", "Milicic", "milica", Category.B, instructor1, candidateRole);
        p8.getAttendedLessons().addAll(allLessons);
        candidateRepository.save(p8);

        Candidate p9 = makePendingCandidate("vladislav@gmail.com", "Vladislav", "Vladicic", "vladislav", Category.A, instructor2, candidateRole);
        p9.getAttendedLessons().addAll(allLessons);
        candidateRepository.save(p9);

        Candidate p10 = makePendingCandidate("biljana@gmail.com", "Biljana", "Biljanic", "biljana", Category.B, instructor1, candidateRole);
        p10.getAttendedLessons().addAll(allLessons);
        candidateRepository.save(p10);

        Candidate p11 = makePendingCandidate("radoslav@gmail.com", "Radoslav", "Radosavljevic", "radoslav", Category.B, instructor2, candidateRole);
        p11.getAttendedLessons().addAll(allLessons);
        candidateRepository.save(p11);

        Candidate p12 = makePendingCandidate("mirjana@gmail.com", "Mirjana", "Mirjanic", "mirjana", Category.B, instructor1, candidateRole);
        p12.getAttendedLessons().addAll(allLessons);
        candidateRepository.save(p12);

        Candidate p13 = makePendingCandidate("branislav@gmail.com", "Branislav", "Brankovic", "branislav", Category.A, instructor2, candidateRole);
        p13.getAttendedLessons().addAll(allLessons);
        candidateRepository.save(p13);

        Candidate p14 = makePendingCandidate("snezana@gmail.com", "Snezana", "Snezic", "snezana", Category.B, instructor1, candidateRole);
        p14.getAttendedLessons().addAll(allLessons);
        candidateRepository.save(p14);

        Candidate p15 = makePendingCandidate("aleksandrap@gmail.com", "Aleksandra", "Petrovic", "aleksandrap", Category.B, instructor2, candidateRole);
        p15.getAttendedLessons().addAll(allLessons);
        candidateRepository.save(p15);

        Candidate p16 = makePendingCandidate("bogdan@gmail.com", "Bogdan", "Bogdanovic", "bogdan", Category.B, instructor1, candidateRole);
        p16.getAttendedLessons().addAll(allLessons);
        candidateRepository.save(p16);

        Candidate p17 = makePendingCandidate("tatjana@gmail.com", "Tatjana", "Tatjanic", "tatjana", Category.A, instructor2, candidateRole);
        p17.getAttendedLessons().addAll(allLessons);
        candidateRepository.save(p17);

        Candidate p18 = makePendingCandidate("nebojsa@gmail.com", "Nebojsa", "Nebojsic", "nebojsa", Category.B, instructor1, candidateRole);
        p18.getAttendedLessons().addAll(allLessons);
        candidateRepository.save(p18);

        Candidate p19 = makePendingCandidate("jasmina@gmail.com", "Jasmina", "Jasminic", "jasmina", Category.B, instructor2, candidateRole);
        p19.getAttendedLessons().addAll(allLessons);
        candidateRepository.save(p19);

        Candidate p20 = makePendingCandidate("dragutin@gmail.com", "Dragutin", "Dragutinovic", "dragutin", Category.B, instructor1, candidateRole);
        p20.getAttendedLessons().addAll(allLessons);
        candidateRepository.save(p20);

        Candidate p21 = makePendingCandidate("ljiljana@gmail.com", "Ljiljana", "Ljiljanic", "ljiljana", Category.A, instructor2, candidateRole);
        p21.getAttendedLessons().addAll(allLessons);
        candidateRepository.save(p21);

        Candidate p22 = makePendingCandidate("goran@gmail.com", "Goran", "Goranic", "goran", Category.B, instructor1, candidateRole);
        p22.getAttendedLessons().addAll(allLessons);
        candidateRepository.save(p22);

        Candidate p23 = makePendingCandidate("zorana@gmail.com", "Zorana", "Zoranic2", "zorana", Category.B, instructor2, candidateRole);
        p23.getAttendedLessons().addAll(allLessons);
        candidateRepository.save(p23);

        Candidate p24 = makePendingCandidate("miodrag@gmail.com", "Miodrag", "Miodrovic", "miodrag", Category.B, instructor1, candidateRole);
        p24.getAttendedLessons().addAll(allLessons);
        candidateRepository.save(p24);

        Candidate p25 = makePendingCandidate("svetlana@gmail.com", "Svetlana", "Svetlanic", "svetlana", Category.A, instructor2, candidateRole);
        p25.getAttendedLessons().addAll(allLessons);
        candidateRepository.save(p25);

        // ---------------- PAST THEORY EXAM (datum prošao) ----------------
        // Uzmemo prvih 20 PENDING kandidata za ispit
        List<Candidate> examCandidates = List.of(
                p1, p2, p3, p4, p5, p6, p7, p8, p9, p10,
                p11, p12, p13, p14, p15, p16, p17, p18, p19, p20
        );

        TheoryExam pastExam = new TheoryExam();
        pastExam.setRegistrationDate(LocalDateTime.now().minusDays(14));
        pastExam.setExamDate(LocalDate.now().minusDays(3));   // 3 dana u prošlosti
        pastExam.setRegisteredBy(professor1);
        pastExam.setCandidates(new java.util.ArrayList<>(examCandidates));
        pastExam.setStatus(TheoryExamStatus.SCHEDULED);       // SCHEDULED + datum prošao = "Enter Results" se prikazuje
        theoryExamRepository.save(pastExam);

        System.out.println("TEST DATA LOADED SUCCESSFULLY");

        System.out.println("TEST DATA LOADED SUCCESSFULLY");
    }

    // ---------------- HELPER METODE ----------------

    private Candidate makePendingCandidate(String email, String name, String lastname, String username,
                                           Category category, Instructor instructor, Role role) {
        Candidate c = new Candidate();
        c.setEmail(email);
        c.setName(name);
        c.setLastname(lastname);
        c.setUsername(username);
        c.setPassword(passwordEncoder.encode("123"));
        c.setEnabled(true);
        c.setRole(role);
        c.setStartOfTraining(LocalDateTime.now().minusMonths(2));
        c.setCategory(category);
        c.setStatus(TrainingStatus.PENDING);
        c.setTheoryCompleted(true);
        c.setInstructor(instructor);
        c.setPreferredLocation("Novi Sad");
        return c;
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