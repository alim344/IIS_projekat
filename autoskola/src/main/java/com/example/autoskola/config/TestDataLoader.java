package com.example.autoskola.config;
import com.example.autoskola.model.*;
import com.example.autoskola.repository.*;
import com.example.autoskola.service.CandidateService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
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
    private final TheoryClassRepository theoryClassRepository;
    private final PracticalExamRepository practicalExamRepository;
    private final CandidateService candidateService;
    private final FuelRecordRepository fuelRecordRepository;
    @Transactional
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
                {"Introduction to traffic regulations", "1"},
                {"Basic traffic rules", "2"},
                {"Traffic signs - general", "3"},
                {"Danger warning signs", "4"},
                {"Mandatory signs", "5"},
                {"Information signs", "6"},
                {"Supplementary panels", "7"},
                {"Traffic light signals", "8"},
                {"Road markings", "9"},
                {"Authorized traffic personnel", "10"},
                {"Right of way at intersections", "11"},
                {"Right of way at intersections - special cases", "12"},
                {"Vehicle speed", "13"},
                {"Distance between vehicles", "14"},
                {"Overtaking", "15"},
                {"Passing oncoming traffic", "16"},
                {"Passing a stopped vehicle", "17"},
                {"Turning", "18"},
                {"Reversing", "19"},
                {"Driving in a roundabout", "20"},
                {"Stopping and parking", "21"},
                {"Stopping and parking - restrictions", "22"},
                {"Use of lights", "23"},
                {"Use of audible and light signals", "24"},
                {"Vehicle load", "25"},
                {"Towing a disabled vehicle", "26"},
                {"Pedestrians in traffic", "27"},
                {"Cyclists in traffic", "28"},
                {"Motorcyclists in traffic", "29"},
                {"Special traffic participants", "30"},
                {"Driving on the highway", "31"},
                {"Driving in tunnels", "32"},
                {"Driving in bad weather conditions", "33"},
                {"Vehicle technical characteristics", "34"},
                {"Active and passive vehicle safety", "35"},
                {"Vehicle handling - basics", "36"},
                {"Dangers of alcohol and drugs in traffic", "37"},
                {"Driver fatigue and illness", "38"},
                {"First aid in traffic", "39"},
                {"Eco-driving and fuel efficiency", "40"}
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

        Vehicle vehicle3 = new Vehicle();
        vehicle3.setRegistrationNumber("BG-789-CC");
        vehicle3.setRegistrationExpiryDate(LocalDate.now().plusYears(1));
        vehicle3.setStatus(VehicleStatus.IN_USE);
        vehicle3.setCurrentMileage(125450);
        vehicleRepository.save(vehicle3);

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
        instructor1.setMaxCapacity(30);
        instructorRepository.save(instructor1);

        Instructor instructor2 = new Instructor();
        instructor2.setEmail("inst2@gmail.com");
        instructor2.setName("Nikola");
        instructor2.setLastname("Nikolic");
        instructor2.setUsername("nikola");
        instructor2.setPassword(passwordEncoder.encode("123"));
        instructor2.setEnabled(true);
        instructor2.setRole(instructorRole);
        instructor2.setMaxCapacity(24);
        instructorRepository.save(instructor2);

        Instructor instructor3 = new Instructor();
        instructor3.setEmail("nare@gmail.com");
        instructor3.setName("Nesa");
        instructor3.setLastname("Radic");
        instructor3.setUsername("naradic");
        instructor3.setPassword(passwordEncoder.encode("123"));
        instructor3.setEnabled(true);
        instructor3.setRole(instructorRole);
        instructor3.setMaxCapacity(27);
        instructorRepository.save(instructor3);

        instructor1.setVehicle(vehicle1);
        vehicle1.setInstructor(instructor1);
        vehicleRepository.save(vehicle1);
        instructorRepository.save(instructor1);

        instructor2.setVehicle(vehicle2);
        vehicle2.setInstructor(instructor2);
        vehicleRepository.save(vehicle2);
        instructorRepository.save(instructor2);

        instructor3.setVehicle(vehicle3);
        vehicle3.setInstructor(instructor3);
        vehicleRepository.save(vehicle3);
        instructorRepository.save(instructor3);


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

        //-------------PASSED CANDIDATES OF INSTRUCTOR 3 for analytics
        for (int i = 1; i <= 10; i++) {
            // 1. Create Candidate
            Candidate pc = new Candidate();
            pc.setEmail("passed" + i + "@gmail.com");
            pc.setName("PassedName" + i);
            pc.setLastname("PassedLastname" + i);
            pc.setUsername("passeduser" + i);
            pc.setPassword(passwordEncoder.encode("123"));
            pc.setEnabled(true);
            pc.setRole(candidateRole);
            // Setting training start to 1.5 years ago so the classes last year make sense
            pc.setStartOfTraining(LocalDateTime.now().minusYears(1).minusMonths(6));
            pc.setCategory(Category.A1); // As requested
            pc.setStatus(TrainingStatus.PASSED);
            pc.setTheoryCompleted(true);
            pc.setInstructor(instructor3);
            pc.setPreferredLocation("Novi Sad");
            candidateRepository.save(pc);

            //kreiramo 20 zavrsenih prakticnih casova za kandidate koji su polozili za statistiku
            LocalDateTime classStartTime = LocalDateTime.now().minusMonths(14);
            for (int j = 1; j <= 20; j++) {
                PracticalClass pClass = new PracticalClass();
                pClass.setCandidate(pc);
                pClass.setInstructor(instructor3);
                pClass.setStartTime(classStartTime);
                pClass.setEndTime(classStartTime.plusHours(1).plusMinutes(30));
                pClass.setAccepted(true);
                pClass.setNotes("Completed legacy class " + j);
                practicalClassRepository.save(pClass);

                // Advance time for the next class
                classStartTime = classStartTime.plusDays(3);
            }

            PracticalExam pExam = new PracticalExam();
            pExam.setDateTime(classStartTime.plusMonths(1));
            pExam.setStatus(ExamStatus.COMPLETED);
            pExam.setCandidate(pc);
            pExam.setInstructor(instructor3);
            pExam.setProfessor(professor1);

            practicalExamRepository.save(pExam);
        }

        //------------- FAILED CANDIDATES FOR ANALYTICS (Instructor 3) -------------

        String[][] failedData = {
                {"Luka", "Bozic", "luka.bozic@gmail.com", "lbozic"},
                {"Matija", "Kovac", "m.kovac99@outlook.com", "mkovac"},
                {"Filip", "Stankovic", "filip.stan@gmail.com", "fistankovic"}
        };
        int[] frequencies = {2, 4, 6};

        for (int i = 0; i < failedData.length; i++) {

            Candidate fc = new Candidate();
            fc.setName(failedData[i][0]);
            fc.setLastname(failedData[i][1]);
            fc.setEmail(failedData[i][2]);
            fc.setUsername(failedData[i][3]);

            fc.setPassword(passwordEncoder.encode("123"));
            fc.setEnabled(true);
            fc.setRole(candidateRole);
            fc.setCategory(Category.A1);


            fc.setStatus(TrainingStatus.PRACTICAL);
            fc.setTheoryCompleted(true);
            fc.setInstructor(instructor3);
            fc.setStartOfTraining(LocalDateTime.now().minusMonths(6));
            fc.setPreferredLocation("Novi Sad");
            candidateRepository.save(fc);

            TimePreference pref = new TimePreference();
            pref.setCandidate(fc);
            LocalDate nextMonday = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));
            //SET TIME PREFs
            if (i == 0) {
                pref.setDate(nextMonday);
                pref.setStartTime(LocalTime.of(8, 0));
                pref.setEndTime(LocalTime.of(10, 0));
            } else if (i == 1) {
                pref.setDate(nextMonday.plusDays(1));
                pref.setStartTime(LocalTime.of(13, 0));
                pref.setEndTime(LocalTime.of(16, 0));
            } else {
                pref.setDate(nextMonday.plusDays(3));
                pref.setStartTime(LocalTime.of(17, 0));
                pref.setEndTime(LocalTime.of(20, 0));
            }

            timePreferenceRepository.save(pref);


            LocalDateTime classTime = LocalDateTime.now().minusMonths(5).with(java.time.DayOfWeek.MONDAY).plusDays(i * 7);

            for (int j = 1; j <= 20; j++) {
                PracticalClass pClass = new PracticalClass();
                pClass.setCandidate(fc);
                pClass.setInstructor(instructor3);
                pClass.setStartTime(classTime.withHour(10 + i));
                pClass.setEndTime(classTime.withHour(11 + i).plusMinutes(30));
                pClass.setAccepted(true);
                pClass.setNotes("Regular training session " + j);
                practicalClassRepository.save(pClass);


                classTime = classTime.plusDays(frequencies[i]);
            }


            PracticalExam fExam = new PracticalExam();
            fExam.setDateTime(classTime.plusDays(3).withHour(9).withMinute(0));
            fExam.setStatus(ExamStatus.FAILED);
            fExam.setCandidate(fc);
            fExam.setInstructor(instructor3);
            fExam.setProfessor(professor2);

            practicalExamRepository.save(fExam);
        }


        Candidate ned1 = candidateService.findByEmail("luka.bozic@gmail.com");
        savePracticalClass(ned1, instructor3, LocalDateTime.of(2026, 2, 24, 10, 30), LocalDateTime.of(2026, 2, 24, 12, 0), true);
        savePracticalClass(ned1, instructor3, LocalDateTime.of(2026, 2, 27, 12, 0), LocalDateTime.of(2026, 2, 27, 13, 30), true);

        Candidate ned2 = candidateService.findByEmail("m.kovac99@outlook.com");
        savePracticalClass(ned2, instructor3, LocalDateTime.of(2026, 2, 24, 12, 0), LocalDateTime.of(2026, 2, 24, 13, 30), true);
        savePracticalClass(ned2, instructor3, LocalDateTime.of(2026, 2, 23, 14, 30), LocalDateTime.of(2026, 2, 23, 16, 0), true);

        Candidate ned3 = candidateService.findByEmail("filip.stan@gmail.com");
        savePracticalClass(ned2, instructor3, LocalDateTime.of(2026, 2, 27, 20, 0), LocalDateTime.of(2026, 2, 27, 21, 30), true);
        savePracticalClass(ned2, instructor3, LocalDateTime.of(2026, 2, 24, 14, 0), LocalDateTime.of(2026, 2, 24, 15, 30), true);




        //ACTIVE CANDIDATES OF INSTRUCTOR 3 - FOR SCHEDULE MAKING
        LocalDate nextMon = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));

       // 1. STEFAN DUKIC
        Candidate cActive1 = new Candidate();
        cActive1.setName("Stefan"); cActive1.setLastname("Dukic");
        cActive1.setEmail("stefan.dukic@gmail.com"); cActive1.setUsername("sdukic");
        cActive1.setPassword(passwordEncoder.encode("123")); cActive1.setEnabled(true);
        cActive1.setRole(candidateRole); cActive1.setCategory(Category.A1);
        cActive1.setStatus(TrainingStatus.PRACTICAL); cActive1.setTheoryCompleted(true);
        cActive1.setInstructor(instructor3); cActive1.setStartOfTraining(LocalDateTime.now().minusMonths(2));
        cActive1.setPreferredLocation("Bulevar oslobodjenja 12");
        candidateRepository.save(cActive1);


        //preferenca za sledecu nedelju
        TimePreference pref = new TimePreference();
        pref.setCandidate(cActive1);
        pref.setDate(nextMon);
        pref.setStartTime(LocalTime.of(8, 0));
        pref.setEndTime(LocalTime.of(13, 0));
        timePreferenceRepository.save(pref);



        //stari casovi
        savePracticalClass(cActive1, instructor3, LocalDateTime.of(2026, 2, 7, 9, 0), LocalDateTime.of(2026, 2, 7, 10, 30), true);
        savePracticalClass(cActive1, instructor3, LocalDateTime.of(2026, 2, 4, 12, 0), LocalDateTime.of(2026, 2, 4, 13, 30), true);
        savePracticalClass(cActive1, instructor3, LocalDateTime.of(2026, 2, 9, 14, 0), LocalDateTime.of(2026, 2, 9, 16, 30), true);
        savePracticalClass(cActive1, instructor3, LocalDateTime.of(2026, 2, 11, 8, 0), LocalDateTime.of(2026, 2, 11, 9, 30), true);
        savePracticalClass(cActive1, instructor3, LocalDateTime.of(2026, 2, 16, 10, 0), LocalDateTime.of(2026, 2, 16, 12, 30), true);
        savePracticalClass(cActive1, instructor3, LocalDateTime.of(2026, 2, 23, 10, 0), LocalDateTime.of(2026, 2, 23, 11, 30), true);
        savePracticalClass(cActive1, instructor3, LocalDateTime.of(2026, 2, 27, 8, 0), LocalDateTime.of(2026, 2, 27, 9, 30), true);


        // 2. ALEKSANDRA POPOVIC
        Candidate cActive2 = new Candidate();
        cActive2.setName("Aleksandra"); cActive2.setLastname("Popovic");
        cActive2.setEmail("aleks.pop9@gmail.com"); cActive2.setUsername("apopovic");
        cActive2.setPassword(passwordEncoder.encode("123")); cActive2.setEnabled(true);
        cActive2.setRole(candidateRole); cActive2.setCategory(Category.A1);
        cActive2.setStatus(TrainingStatus.PRACTICAL); cActive2.setTheoryCompleted(true);
        cActive2.setInstructor(instructor3); cActive2.setStartOfTraining(LocalDateTime.now().minusMonths(2));
        cActive2.setPreferredLocation("Fruskogorska 4");
        candidateRepository.save(cActive2);

        TimePreference pref1 = new TimePreference();
        pref1.setCandidate(cActive2);
        pref1.setDate(nextMon.plusDays(3));
        pref1.setStartTime(LocalTime.of(8, 0));
        pref1.setEndTime(LocalTime.of(15, 0));
        timePreferenceRepository.save(pref1);

        savePracticalClass(cActive2, instructor3, LocalDateTime.of(2026, 2, 2, 13, 0), LocalDateTime.of(2026, 2, 2, 14, 30), true);
        savePracticalClass(cActive2, instructor3, LocalDateTime.of(2026, 2, 5, 12, 0), LocalDateTime.of(2026, 2, 5, 13, 30), true);
        savePracticalClass(cActive2, instructor3, LocalDateTime.of(2026, 2, 10, 15, 0), LocalDateTime.of(2026, 2, 10, 16, 30), true);
        savePracticalClass(cActive2, instructor3, LocalDateTime.of(2026, 2, 12, 11, 0), LocalDateTime.of(2026, 2, 12, 12, 30), true);
        savePracticalClass(cActive2, instructor3, LocalDateTime.of(2026, 2, 17, 10, 0), LocalDateTime.of(2026, 2, 17, 11, 30), true);
        savePracticalClass(cActive2, instructor3, LocalDateTime.of(2026, 2, 23, 13, 0), LocalDateTime.of(2026, 2, 23, 14, 30), true);
        savePracticalClass(cActive2, instructor3, LocalDateTime.of(2026, 2, 25, 11, 0), LocalDateTime.of(2026, 2, 25, 12, 30), true);


// 3. NIKOLA TESIC
        Candidate cActive3 = new Candidate();
        cActive3.setName("Nikola"); cActive3.setLastname("Tesic");
        cActive3.setEmail("ntesic@outlook.com"); cActive3.setUsername("ntesic");
        cActive3.setPassword(passwordEncoder.encode("123")); cActive3.setEnabled(true);
        cActive3.setRole(candidateRole); cActive3.setCategory(Category.A1);
        cActive3.setInstructor(instructor3); cActive3.setStatus(TrainingStatus.PRACTICAL);
        cActive3.setTheoryCompleted(true); cActive3.setPreferredLocation("Cara Dusana 25");
        candidateRepository.save(cActive3);

        TimePreference pref2 = new TimePreference();
        pref2.setCandidate(cActive3);
        pref2.setDate(nextMon.plusDays(1));
        pref2.setStartTime(LocalTime.of(12, 0));
        pref2.setEndTime(LocalTime.of(15, 0));
        timePreferenceRepository.save(pref2);


        savePracticalClass(cActive3, instructor3, LocalDateTime.of(2026, 2, 2, 10, 0), LocalDateTime.of(2026, 2, 2, 11, 30), true);
        savePracticalClass(cActive3, instructor3, LocalDateTime.of(2026, 2, 8, 11, 30), LocalDateTime.of(2026, 2, 8, 12, 0), true);
        savePracticalClass(cActive3, instructor3, LocalDateTime.of(2026, 2, 9, 12, 30), LocalDateTime.of(2026, 2, 9, 14, 0), true);
        savePracticalClass(cActive3, instructor3, LocalDateTime.of(2026, 2, 11, 10, 0), LocalDateTime.of(2026, 2, 11, 11, 30), true);
        savePracticalClass(cActive3, instructor3, LocalDateTime.of(2026, 2, 16, 8, 0), LocalDateTime.of(2026, 2, 16, 9, 30), true);
        savePracticalClass(cActive3, instructor3, LocalDateTime.of(2026, 2, 25, 9, 30), LocalDateTime.of(2026, 2, 25, 11, 00), true);
        savePracticalClass(cActive3, instructor3, LocalDateTime.of(2026, 2, 27, 15, 0), LocalDateTime.of(2026, 2, 25, 16, 30), true);

// 4. MILICA JOVIC
        Candidate cActive4 = new Candidate();
        cActive4.setName("Milica"); cActive4.setLastname("Jovic");
        cActive4.setEmail("mjovic@gmail.com"); cActive4.setUsername("mjovic");
        cActive4.setPassword(passwordEncoder.encode("123")); cActive4.setEnabled(true);
        cActive4.setRole(candidateRole); cActive4.setCategory(Category.A1);
        cActive4.setInstructor(instructor3); cActive4.setStatus(TrainingStatus.PRACTICAL);
        cActive4.setTheoryCompleted(true); cActive4.setPreferredLocation("Strazilovska 10");
        candidateRepository.save(cActive4);

        TimePreference pref3 = new TimePreference();
        pref3.setCandidate(cActive4);
        pref3.setDate(nextMon.plusDays(3));
        pref3.setStartTime(LocalTime.of(9, 0));
        pref3.setEndTime(LocalTime.of(14, 30));
        timePreferenceRepository.save(pref3);

        // Late classes
        savePracticalClass(cActive4, instructor3, LocalDateTime.of(2026, 2, 3, 9, 0), LocalDateTime.of(2026, 2, 3, 10, 30), true);
        savePracticalClass(cActive4, instructor3, LocalDateTime.of(2026, 2, 6, 13, 0), LocalDateTime.of(2026, 2, 6, 14, 30), true);
        savePracticalClass(cActive4, instructor3, LocalDateTime.of(2026, 2, 10, 19, 0), LocalDateTime.of(2026, 2, 10, 20, 30), true);
        savePracticalClass(cActive4, instructor3, LocalDateTime.of(2026, 2, 26, 15, 0), LocalDateTime.of(2026, 2, 26, 16, 30), true);
        savePracticalClass(cActive4, instructor3, LocalDateTime.of(2026, 3, 1, 9, 0), LocalDateTime.of(2026, 3, 1, 10, 30), true);


// 5. PAVLE SIMIC
        Candidate cActive5 = new Candidate();
        cActive5.setName("Pavle"); cActive5.setLastname("Simic");
        cActive5.setEmail("psimic@proton.me"); cActive5.setUsername("psimic");
        cActive5.setPassword(passwordEncoder.encode("123")); cActive5.setEnabled(true);
        cActive5.setRole(candidateRole); cActive5.setCategory(Category.A1);
        cActive5.setInstructor(instructor3); cActive5.setStatus(TrainingStatus.PRACTICAL);
        cActive5.setTheoryCompleted(true); cActive5.setPreferredLocation("Jevrejska 2");
        candidateRepository.save(cActive5);

        TimePreference pref4 = new TimePreference();
        pref4.setCandidate(cActive5);
        pref4.setDate(nextMon.plusDays(2));
        pref4.setStartTime(LocalTime.of(9, 0));
        pref4.setEndTime(LocalTime.of(17, 30));
        timePreferenceRepository.save(pref4);


        savePracticalClass(cActive5, instructor3, LocalDateTime.of(2026, 2, 2, 8, 0), LocalDateTime.of(2026, 2, 2, 9, 30), true);
        savePracticalClass(cActive5, instructor3, LocalDateTime.of(2026, 2, 9, 9, 0), LocalDateTime.of(2026, 2, 9, 10, 30), true);
        savePracticalClass(cActive5, instructor3, LocalDateTime.of(2026, 2, 24, 8, 0), LocalDateTime.of(2026, 2, 24, 9, 30), true);
        savePracticalClass(cActive5, instructor3, LocalDateTime.of(2026, 2, 26, 8, 30), LocalDateTime.of(2026, 2, 26, 10, 0), true);


// 6. TEODORA LAZIC
        Candidate cActive6 = new Candidate();
        cActive6.setName("Teodora"); cActive6.setLastname("Lazic");
        cActive6.setEmail("tlazic@gmail.com"); cActive6.setUsername("tlazic");
        cActive6.setPassword(passwordEncoder.encode("123")); cActive6.setEnabled(true);
        cActive6.setRole(candidateRole); cActive6.setCategory(Category.A1);
        cActive6.setInstructor(instructor3); cActive6.setStatus(TrainingStatus.PRACTICAL);
        cActive6.setTheoryCompleted(true); cActive6.setPreferredLocation("Hadzi Ruvimova 15");
        candidateRepository.save(cActive6);

        TimePreference pref5 = new TimePreference();
        pref5.setCandidate(cActive6);
        pref5.setDate(nextMon.plusDays(5));
        pref5.setStartTime(LocalTime.of(8, 0));
        pref5.setEndTime(LocalTime.of(12, 30));
        timePreferenceRepository.save(pref5);

        savePracticalClass(cActive6, instructor3, LocalDateTime.of(2026, 2, 4, 14, 0), LocalDateTime.of(2026, 2, 4, 15, 30), true);
        savePracticalClass(cActive6, instructor3, LocalDateTime.of(2026, 2, 11, 14, 0), LocalDateTime.of(2026, 2, 11, 15, 30), true);
        savePracticalClass(cActive6, instructor3, LocalDateTime.of(2026, 2, 25, 15, 0), LocalDateTime.of(2026, 2, 25, 16, 30), true);
        savePracticalClass(cActive6, instructor3, LocalDateTime.of(2026, 2, 23, 8, 30), LocalDateTime.of(2026, 2, 23, 10, 0), true);


// 7. VIKTOR SAVIC
        Candidate cActive7 = new Candidate();
        cActive7.setName("Viktor"); cActive7.setLastname("Savic");
        cActive7.setEmail("vsavic@gmail.com"); cActive7.setUsername("vsavic");
        cActive7.setPassword(passwordEncoder.encode("123")); cActive7.setEnabled(true);
        cActive7.setRole(candidateRole); cActive7.setCategory(Category.A1);
        cActive7.setInstructor(instructor3); cActive7.setStatus(TrainingStatus.PRACTICAL);
        cActive7.setTheoryCompleted(true); cActive7.setPreferredLocation("Sutjeska 3");
        candidateRepository.save(cActive7);

        TimePreference pref6 = new TimePreference();
        pref6.setCandidate(cActive7);
        pref6.setDate(nextMon.plusDays(4));
        pref6.setStartTime(LocalTime.of(8, 0));
        pref6.setEndTime(LocalTime.of(17, 0));
        timePreferenceRepository.save(pref6);

        savePracticalClass(cActive7, instructor3, LocalDateTime.of(2026, 2, 6, 9, 0), LocalDateTime.of(2026, 2, 6, 10, 30), true);
        savePracticalClass(cActive7, instructor3, LocalDateTime.of(2026, 2, 13, 9, 0), LocalDateTime.of(2026, 2, 13, 10, 30), true);
        savePracticalClass(cActive7, instructor3, LocalDateTime.of(2026, 2, 28, 9, 0), LocalDateTime.of(2026, 2, 28, 10, 30), true);
        savePracticalClass(cActive7, instructor3, LocalDateTime.of(2026, 2, 26, 11, 0), LocalDateTime.of(2026, 2, 26, 12, 30), true);


        // --- CANDIDATES WITH PASSED THEORY WITHOUT AN INSTRUCTOR ---
        Candidate candNoProfessor1 = makeCandidate("eren@gmail.com", "Eren", "Bajunovic", "eren", Category.B, null, candidateRole);
        candNoProfessor1.setStatus(TrainingStatus.PRACTICAL);
        candNoProfessor1.setTheoryCompleted(true);
        candidateRepository.save(candNoProfessor1);

        Candidate candNoProfessor2 = makeCandidate("mikasa@gmail.com", "Mikasa", "Civic", "mikasa", Category.B, null, candidateRole);
        candNoProfessor2.setStatus(TrainingStatus.PRACTICAL);
        candNoProfessor2.setTheoryCompleted(true);
        candidateRepository.save(candNoProfessor2);

        Candidate candNoProfessor3 = makeCandidate("armin@gmail.com", "Armin", "Jekic", "armin", Category.B, null, candidateRole);
        candNoProfessor3.setStatus(TrainingStatus.PRACTICAL);
        candNoProfessor3.setTheoryCompleted(true);
        candidateRepository.save(candNoProfessor3);

        Candidate candNoProfessor4 = makeCandidate("kaneki@gmail.com", "Kaneki", "Jelic", "kaneki", Category.B, null, candidateRole);
        candNoProfessor4.setStatus(TrainingStatus.PRACTICAL);
        candNoProfessor4.setTheoryCompleted(true);
        candidateRepository.save(candNoProfessor4);

        Candidate candNoProfessor5 = makeCandidate("noel@gmail.com", "Noel", "Katic", "noel", Category.B, null, candidateRole);
        candNoProfessor5.setStatus(TrainingStatus.PRACTICAL);
        candNoProfessor5.setTheoryCompleted(true);
        candidateRepository.save(candNoProfessor5);


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
        pastExam.setExamDate(LocalDate.now().minusDays(3));
        pastExam.setRegisteredBy(professor1);
        pastExam.setCandidates(new java.util.ArrayList<>(examCandidates));
        pastExam.setStatus(TheoryExamStatus.SCHEDULED);
        theoryExamRepository.save(pastExam);

        Candidate ana = candidateRepository.getByEmail("ana@gmail.com");

        if (ana != null) {

            List<TheoryLesson> lessons1 = theoryLessonRepository.findAll();

            ana = candidateRepository.findById(ana.getId())
                    .orElseThrow(() -> new RuntimeException("Ana not found"));

            for (int i = 0; i < 5; i++) {
                TheoryLesson lesson = lessons1.get(i);

                TheoryClass pastTheory = new TheoryClass();
                pastTheory.setProfessor(professor1);
                pastTheory.setTheoryLesson(lesson);
                pastTheory.setStartTime(LocalDateTime.now().minusDays(7 + i * 2).withHour(10).withMinute(0));
                pastTheory.setEndTime(LocalDateTime.now().minusDays(7 + i * 2).withHour(12).withMinute(0));
                pastTheory.setCapacity(20);
                pastTheory.setEnrolledStudents(1);

                List<Candidate> students = new ArrayList<>();
                students.add(ana);
                pastTheory.setStudents(students);

                theoryClassRepository.save(pastTheory);

                if (lesson.getId() != 1) {
                    ana.getAttendedLessons().add(lesson);
                }
            }

            candidateRepository.save(ana);
            System.out.println("✓ Created 5 past theory classes for Ana with attendance");

            // ---------------- FUEL RECORDS ----------------
// Kreiramo test podatke za točenje goriva - 4-5 točenja po vozilu
            List<FuelRecord> fuelRecords = new ArrayList<>();

// Trenutni datum za reference
            LocalDate today = LocalDate.now();

// VOZILO 1 (NS-123-AA) - 5 točenja
            Vehicle veh1 = vehicleRepository.findByRegistrationNumber("NS-123-AA").orElse(vehicle1);
            FuelRecord fr1_1 = new FuelRecord();
            fr1_1.setRefuelDate(today.minusMonths(2).minusDays(3));
            fr1_1.setLiters(42.5);
            fr1_1.setTotalCost(7125.75); // 167.66 RSD/L približno
            fr1_1.setMileageAtRefuel(45200);
            fr1_1.setVehicle(veh1);
            fr1_1.setInstructor(instructor1); // Marko Markovic
            fuelRecords.add(fr1_1);

            FuelRecord fr1_2 = new FuelRecord();
            fr1_2.setRefuelDate(today.minusMonths(1).minusDays(15));
            fr1_2.setLiters(38.2);
            fr1_2.setTotalCost(6450.50);
            fr1_2.setMileageAtRefuel(45850);
            fr1_2.setVehicle(veh1);
            fr1_2.setInstructor(instructor1); // Marko Markovic
            fuelRecords.add(fr1_2);

            FuelRecord fr1_3 = new FuelRecord();
            fr1_3.setRefuelDate(today.minusMonths(1).minusDays(2));
            fr1_3.setLiters(45.0);
            fr1_3.setTotalCost(7650.00);
            fr1_3.setMileageAtRefuel(46520);
            fr1_3.setVehicle(veh1);
            fr1_3.setInstructor(instructor1); // Marko Markovic
            fuelRecords.add(fr1_3);

            FuelRecord fr1_4 = new FuelRecord();
            fr1_4.setRefuelDate(today.minusDays(10));
            fr1_4.setLiters(41.8);
            fr1_4.setTotalCost(7315.00);
            fr1_4.setMileageAtRefuel(47180);
            fr1_4.setVehicle(veh1);
            fr1_4.setInstructor(instructor3); // Nesa Radic - drugi instruktor
            fuelRecords.add(fr1_4);

            FuelRecord fr1_5 = new FuelRecord();
            fr1_5.setRefuelDate(today.minusDays(2));
            fr1_5.setLiters(44.3);
            fr1_5.setTotalCost(7965.40);
            fr1_5.setMileageAtRefuel(47850);
            fr1_5.setVehicle(veh1);
            fr1_5.setInstructor(instructor1); // Marko Markovic
            fuelRecords.add(fr1_5);

// VOZILO 2 (SU-456-BB) - 5 točenja
            Vehicle veh2 = vehicleRepository.findByRegistrationNumber("SU-456-BB").orElse(vehicle2);
            FuelRecord fr2_1 = new FuelRecord();
            fr2_1.setRefuelDate(today.minusMonths(3).minusDays(5));
            fr2_1.setLiters(52.0);
            fr2_1.setTotalCost(8580.00); // 165 RSD/L
            fr2_1.setMileageAtRefuel(81200);
            fr2_1.setVehicle(veh2);
            fr2_1.setInstructor(instructor2); // Nikola Nikolic
            fuelRecords.add(fr2_1);

            FuelRecord fr2_2 = new FuelRecord();
            fr2_2.setRefuelDate(today.minusMonths(2).minusDays(10));
            fr2_2.setLiters(47.5);
            fr2_2.setTotalCost(8027.50); // 169 RSD/L
            fr2_2.setMileageAtRefuel(81950);
            fr2_2.setVehicle(veh2);
            fr2_2.setInstructor(instructor2); // Nikola Nikolic
            fuelRecords.add(fr2_2);

            FuelRecord fr2_3 = new FuelRecord();
            fr2_3.setRefuelDate(today.minusMonths(1).minusDays(8));
            fr2_3.setLiters(49.8);
            fr2_3.setTotalCost(8665.20); // 174 RSD/L
            fr2_3.setMileageAtRefuel(82780);
            fr2_3.setVehicle(veh2);
            fr2_3.setInstructor(instructor2); // Nikola Nikolic
            fuelRecords.add(fr2_3);

            FuelRecord fr2_4 = new FuelRecord();
            fr2_4.setRefuelDate(today.minusDays(18));
            fr2_4.setLiters(44.2);
            fr2_4.setTotalCost(7940.75); // ~179.65 RSD/L
            fr2_4.setMileageAtRefuel(83560);
            fr2_4.setVehicle(veh2);
            fr2_4.setInstructor(instructor1); // Marko Markovic - drugi instruktor
            fuelRecords.add(fr2_4);

            FuelRecord fr2_5 = new FuelRecord();
            fr2_5.setRefuelDate(today.minusDays(5));
            fr2_5.setLiters(51.3);
            fr2_5.setTotalCost(9387.90); // ~183 RSD/L
            fr2_5.setMileageAtRefuel(84320);
            fr2_5.setVehicle(veh2);
            fr2_5.setInstructor(instructor2); // Nikola Nikolic
            fuelRecords.add(fr2_5);

// VOZILO 3 (BG-789-CC) - 4 točenja
            Vehicle veh3 = vehicleRepository.findByRegistrationNumber("BG-789-CC").orElse(vehicle3);
            FuelRecord fr3_1 = new FuelRecord();
            fr3_1.setRefuelDate(today.minusMonths(2).minusDays(20));
            fr3_1.setLiters(48.7);
            fr3_1.setTotalCost(7941.62); // ~163.07 RSD/L
            fr3_1.setMileageAtRefuel(125450);
            fr3_1.setVehicle(veh3);
            fr3_1.setInstructor(instructor3); // Nesa Radic
            fuelRecords.add(fr3_1);

            FuelRecord fr3_2 = new FuelRecord();
            fr3_2.setRefuelDate(today.minusMonths(1).minusDays(12));
            fr3_2.setLiters(52.3);
            fr3_2.setTotalCost(8943.30); // ~171 RSD/L
            fr3_2.setMileageAtRefuel(126320);
            fr3_2.setVehicle(veh3);
            fr3_2.setInstructor(instructor2); // Nikola Nikolic - drugi instruktor
            fuelRecords.add(fr3_2);

            FuelRecord fr3_3 = new FuelRecord();
            fr3_3.setRefuelDate(today.minusDays(22));
            fr3_3.setLiters(46.8);
            fr3_3.setTotalCost(8424.00); // 180 RSD/L
            fr3_3.setMileageAtRefuel(127150);
            fr3_3.setVehicle(veh3);
            fr3_3.setInstructor(instructor3); // Nesa Radic
            fuelRecords.add(fr3_3);

            FuelRecord fr3_4 = new FuelRecord();
            fr3_4.setRefuelDate(today.minusDays(8));
            fr3_4.setLiters(50.1);
            fr3_4.setTotalCost(9268.50); // 185 RSD/L
            fr3_4.setMileageAtRefuel(128050);
            fr3_4.setVehicle(veh3);
            fr3_4.setInstructor(instructor1); // Marko Markovic - drugi instruktor
            fuelRecords.add(fr3_4);

// Dodajemo dodatno 5-to točenje za vozilo 3 (da bude 5 umesto 4)
            FuelRecord fr3_5 = new FuelRecord();
            fr3_5.setRefuelDate(today.minusDays(1));
            fr3_5.setLiters(39.5);
            fr3_5.setTotalCost(7505.00); // 190 RSD/L
            fr3_5.setMileageAtRefuel(128650);
            fr3_5.setVehicle(veh3);
            fr3_5.setInstructor(instructor3); // Nesa Radic
            fuelRecords.add(fr3_5);

// Čuvamo sve zapise u bazu
            fuelRecordRepository.saveAll(fuelRecords);
            System.out.println("✓ Created " + fuelRecords.size() + " fuel records across all vehicles");

        }

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