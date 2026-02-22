package com.example.autoskola.service;

import com.example.autoskola.dto.ManualTheoryClassRequestDTO;
import com.example.autoskola.model.*;
import com.example.autoskola.repository.CandidateRepository;
import com.example.autoskola.repository.ProfessorRepository;
import com.example.autoskola.repository.TheoryClassRepository;
import com.example.autoskola.repository.TheoryLessonRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TheorySchedulingService {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private TheoryLessonRepository theoryLessonRepository;

    @Autowired
    private TheoryClassRepository theoryClassRepository;

    private static final int MAX_STUDENTS_PER_CLASS = 20;

    private static final List<DayOfWeek> TEACHING_DAYS = List.of(
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY
    );

    @Transactional
    public List<TheoryClass> generateSchedule() {
        List<Candidate> allCandidates = candidateRepository.findByStatus(TrainingStatus.THEORY);

        List<Professor> professors = professorRepository.findAll();

        if (professors.isEmpty()) {
            throw new RuntimeException("No professors available");
        }

        List<TheoryClass> generatedClasses = new ArrayList<>();

        LocalDate nextMonday = LocalDate.now().with(DayOfWeek.MONDAY).plusWeeks(1);
        LocalDate nextFriday = nextMonday.plusDays(4);

        List<LocalDateTime> availableSlots = new ArrayList<>();

        for (LocalDate date = nextMonday; !date.isAfter(nextFriday); date = date.plusDays(1)) {
            for (FixedSlot slot : FixedSlot.values()) {
                availableSlots.add(LocalDateTime.of(date, slot.getStartTime()));
            }
        }

        System.out.println("\n=== Generating schedule for " + allCandidates.size() + " candidates ===");
        System.out.println("Week: " + nextMonday + " to " + nextFriday);

        Map<FixedSlot, List<Candidate>> bySlot = groupBySlot(allCandidates);

        List<TheoryLesson> lessons = theoryLessonRepository.findAllByOrderByOrderNumberAsc();

        if (lessons.isEmpty()) {
            throw new RuntimeException("No lessons found in database");
        }

        Map<Long, Integer> candidateWeeklyCount = new HashMap<>(); //koliko zakazanih casova ima kandidat

        allCandidates.forEach(c -> candidateWeeklyCount.put(c.getId(), 0));

        Set<LocalDateTime> occupiedSlots = new HashSet<>();

        for (Map.Entry<FixedSlot, List<Candidate>> slotEntry : bySlot.entrySet()) { // MORNING, AFTERNOON EVENING
            FixedSlot slot = slotEntry.getKey();
            List<Candidate> slotCandidates = slotEntry.getValue(); // svi kandidati kojima odgovara trenutni slot npr MORNING

            System.out.println("\n--- Slot: " + slot + " (" + slotCandidates.size() + " candidates) ---");

            List<LocalDateTime> slotsForThisFixedSlot = availableSlots.stream()
                    .filter(dt -> dt.toLocalTime().equals(slot.getStartTime()))
                    .sorted() // sort po datumu
                    .collect(Collectors.toList()); //od svih termina filtriram samo onaj koji je isti kao onaj sto trenutno gledamo a da je pritom slobodan
            // npr MORNING  u pon, uto , sre, cet, pet

            for (TheoryLesson lesson : lessons) {

                List<Candidate> needLesson = slotCandidates.stream()
                        .filter(c -> !c.getAttendedLessons().contains(lesson)) // da kandidat nije vec slusao lekciju
                        .filter(c -> candidateWeeklyCount.getOrDefault(c.getId(), 0) < 2) // pgranicenje da je samo 2 casa nedeljno
                        .collect(Collectors.toList());

                if (needLesson.isEmpty()) {
                    continue;
                }

                List<List<Candidate>> batches = splitIntoBatches(needLesson, MAX_STUDENTS_PER_CLASS);

                System.out.println("Split into " + batches.size() + " batch(es)");

                for (List<Candidate> batch : batches) {

                    // PRVI SLOBODAN TERMIN za ovaj slot
                    LocalDateTime classTime = null;
                    for (LocalDateTime slotTime : slotsForThisFixedSlot) {
                        if (!occupiedSlots.contains(slotTime)) {
                            classTime = slotTime;
                            break;
                        }
                    }
                    if (classTime == null) {
                        System.out.println("No more available slots for this FixedSlot");
                        break; // nema više slobodnih termina ovog slota
                    }

                    // nalazi profesora koji je slobodan u tom terminu a da ima najmanje zakazanih casova
                    Professor professor = findProfessorWithLeastClasses(professors, generatedClasses);

                    if (professor == null) {
                        System.out.println("No free professor at " + classTime);
                        continue;
                    }

                    occupiedSlots.add(classTime);

                    TheoryClass theoryClass = new TheoryClass();
                    theoryClass.setTheoryLesson(lesson);
                    theoryClass.setProfessor(professor);
                    theoryClass.setStartTime(classTime);
                    theoryClass.setEndTime(classTime.plusHours(2));
                    theoryClass.setCapacity(MAX_STUDENTS_PER_CLASS);
                    theoryClass.setEnrolledStudents(batch.size());
                    theoryClass.setStudents(new ArrayList<>(batch));

                    TheoryClass saved = theoryClassRepository.save(theoryClass);
                    generatedClasses.add(saved);

                    System.out.println("Created class at " + classTime +
                            " with " + batch.size() + " students, Prof: " + professor.getName());

                    for (Candidate candidate : batch) {
                        candidateWeeklyCount.merge(candidate.getId(), 1, Integer::sum);
                    }
                }
            }
        }

        System.out.println("\n=== Total classes generated: " + generatedClasses.size() + " ===");
        return generatedClasses;
    }

    private Professor findProfessorWithLeastClasses(List<Professor> professors, List<TheoryClass> generatedClasses) {
        Map<Long, Long> professorClassCount = new HashMap<>();

        // koliko casova ima svaki profesor
        for (TheoryClass tc : generatedClasses) {
            Long profId = tc.getProfessor().getId();
            professorClassCount.merge(profId, 1L, Long::sum);
        }

        // prof sa najmanje casova se uzima
        return professors.stream()
                .min(Comparator.comparingLong(p -> professorClassCount.getOrDefault(p.getId(), 0L)))
                .orElse(null);
    }

    private Map<FixedSlot, List<Candidate>> groupBySlot(List<Candidate> candidates) {
        Map<FixedSlot, List<Candidate>> groups = new EnumMap<>(FixedSlot.class);

        for (Candidate candidate : candidates) {
            FixedSlot slot = determineSlot(candidate);
            groups.computeIfAbsent(slot, k -> new ArrayList<>()).add(candidate);
        }

        return groups;
    }

    private FixedSlot determineSlot(Candidate candidate) {
        if (candidate.getTimePreference() == null) {
            return getLeastPopulatedSlot(); // Default
        }

        LocalTime prefStart = candidate.getTimePreference().getStartTime();
        LocalTime prefEnd = candidate.getTimePreference().getEndTime();

        for (FixedSlot slot : FixedSlot.values()) {
            if (timesOverlap(slot.getStartTime(), slot.getEndTime(), prefStart, prefEnd)) {
                return slot;
            }
        }

        return getLeastPopulatedSlot();
    }

    private FixedSlot getLeastPopulatedSlot() {
        LocalDate nextMonday = LocalDate.now().with(DayOfWeek.MONDAY).plusWeeks(1);
        LocalDateTime start = nextMonday.atStartOfDay();
        LocalDateTime end = nextMonday.plusWeeks(1).atStartOfDay();

        Map<FixedSlot, Long> slotCounts = new EnumMap<>(FixedSlot.class);
        for (FixedSlot slot : FixedSlot.values()) {
            slotCounts.put(slot, 0L);
        }

        List<TheoryClass> nextWeekClasses = theoryClassRepository.findByStartTimeBetween(start, end);
        for (TheoryClass tc : nextWeekClasses) {
            LocalTime classTime = tc.getStartTime().toLocalTime();
            for (FixedSlot slot : FixedSlot.values()) {
                if (slot.getStartTime().equals(classTime)) {
                    slotCounts.merge(slot, (long) tc.getEnrolledStudents(), Long::sum);
                }
            }
        }

        return slotCounts.entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(FixedSlot.AFTERNOON);
    }

    private boolean timesOverlap(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {
        return !(end1.isBefore(start2) || start1.isAfter(end2));
    }

    private List<List<Candidate>> splitIntoBatches(List<Candidate> candidates, int batchSize) {
        List<List<Candidate>> batches = new ArrayList<>();
        for (int i = 0; i < candidates.size(); i += batchSize) {
            batches.add(new ArrayList<>(
                    candidates.subList(i, Math.min(i + batchSize, candidates.size()))
            ));
        }
        return batches;
    }

    @Transactional
    public TheoryClass createManualClass(ManualTheoryClassRequestDTO requestDTO) {
        if(requestDTO.getDate() == null || requestDTO.getDate().isBefore(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Date must be valid. Unable to schedule class");
        }

        if (requestDTO.getSlot() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Slot is required (MORNING, AFTERNOON, EVENING)");
        }

        if (requestDTO.getCandidateIds() == null || requestDTO.getCandidateIds().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "At least one candidate is required");
        }

        if (requestDTO.getCandidateIds().size() > MAX_STUDENTS_PER_CLASS) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Maximum " + MAX_STUDENTS_PER_CLASS + " candidates per class");
        }

        TheoryLesson lesson = theoryLessonRepository.findById(requestDTO.getLessonId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lesson not found"));

        Professor professor = professorRepository.findById(requestDTO.getProfessorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Professor not found"));

        List<Candidate> candidates = candidateRepository.findAllById(requestDTO.getCandidateIds());

        if (candidates.size() != requestDTO.getCandidateIds().size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "One or more candidate IDs are invalid");
        }

        List<String> notInTheory = candidates.stream()
                .filter(c -> c.getStatus() != TrainingStatus.THEORY)
                .map(c -> c.getName() + " " + c.getLastname())
                .collect(Collectors.toList());

        if(!notInTheory.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Not in THEORY status: " + String.join(", ", notInTheory));
        }

        List<String> alreadyAttended = candidates.stream()
                .filter(c -> c.getAttendedLessons().stream().anyMatch(l -> l.getId().equals(lesson.getId())))
                .map(c -> c.getName() + " " + c.getLastname())
                .collect(Collectors.toList());

        if(!alreadyAttended.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Already attended this lesson: " + String.join(", ", alreadyAttended));
        }

        for (Candidate c : candidates) {
            boolean alreadyEnrolled = theoryClassRepository.existsByStudentsIdAndTheoryLessonId(c.getId(), lesson.getId());
            if (alreadyEnrolled) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        c.getName() + " " + c.getLastname() + " is already enrolled in another class for: " + lesson.getName());
            }
        }

        LocalDateTime startTime = LocalDateTime.of(requestDTO.getDate(), requestDTO.getSlot().getStartTime());
        LocalDateTime endTime = LocalDateTime.of(requestDTO.getDate(), requestDTO.getSlot().getEndTime());

        boolean professorBusy = theoryClassRepository.existsByProfessorIdAndStartTime(professor.getId(), startTime);
        if (professorBusy) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Professor " + professor.getName() + " " + professor.getLastname() + " already has a class at this time");
        }

        List<String> candidatesWithConflict = new ArrayList<>();
        for (Candidate candidate : candidates) {
            boolean hasClassAtTime = theoryClassRepository.existsByStudentsIdAndStartTime(
                    candidate.getId(), startTime);

            if (hasClassAtTime) {
                candidatesWithConflict.add(candidate.getName() + " " + candidate.getLastname());
            }
        }

        if (!candidatesWithConflict.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Following candidates already have a class at this time: " +
                            String.join(", ", candidatesWithConflict));
        }

        TheoryClass tc = new TheoryClass();
        tc.setStartTime(startTime);
        tc.setEndTime(endTime);
        tc.setProfessor(professor);
        tc.setTheoryLesson(lesson);
        tc.setCapacity(MAX_STUDENTS_PER_CLASS);
        tc.setEnrolledStudents(candidates.size());
        tc.setStudents(new ArrayList<>(candidates));

        return theoryClassRepository.save(tc);
    }


}
