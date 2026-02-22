package com.example.autoskola.service;

import com.example.autoskola.dto.AnalyticsDTO;
import com.example.autoskola.model.*;
import com.example.autoskola.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnalyticsService {
    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private TheoryClassRepository theoryClassRepository;

    @Autowired
    private PracticalClassRepository practicalClassRepository;

    @Autowired
    private TheoryLessonRepository theoryLessonRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    public AnalyticsDTO getAnalytics(LocalDate startDate, LocalDate endDate) {
        AnalyticsDTO analytics = new AnalyticsDTO();

        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(23, 59, 59);

        analytics.setCandidatePreferences(getCandidatePreferences());

        List<TheoryClass> theoryClasses = theoryClassRepository.findByStartTimeBetween(start, end);
        analytics.setTotalTheoryClasses(theoryClasses.size());
        analytics.setTheoryClassesBySlot(getTheoryClassesBySlot(theoryClasses));
        analytics.setAverageTheoryClassOccupancy(getAverageOccupancy(theoryClasses));
        analytics.setOccupancyBySlot(getOccupancyBySlot(theoryClasses));
        analytics.setAttendanceRateByLesson(getAttendanceRateByLesson());

        List<PracticalClass> practicalClasses = practicalClassRepository.findByStartTimeBetween(start, end);
        analytics.setTotalPracticalClasses(practicalClasses.size());
        analytics.setPracticalClassesBySlot(getPracticalClassesBySlot(practicalClasses));
        analytics.setPracticalClassesByInstructor(getPracticalClassesByInstructor(practicalClasses));
        analytics.setCompletedPracticalByInstructor(getCompletedPracticalByInstructor());

        List<Candidate> allCandidates = candidateRepository.findAll();
        analytics.setTotalCandidates(allCandidates.size());
        analytics.setCandidatesByCategory(getCandidatesByCategory(allCandidates));
        analytics.setCandidatesByStatus(getCandidatesByStatus(allCandidates));
        analytics.setAverageLessonsCompleted(getAverageLessonsCompleted(allCandidates));
        analytics.setCandidatesReadyForExam(
                (int) allCandidates.stream()
                        .filter(c -> c.getStatus() == TrainingStatus.PENDING)
                        .count()
        );

        return analytics;
    }

    private Map<String, Integer> getCandidatePreferences() {
        List<Candidate> candidates = candidateRepository.findAll();
        Map<String, Integer> preferences = new HashMap<>();

        for (FixedSlot slot : FixedSlot.values()) {
            preferences.put(slot.name(), 0);
        }

        for (Candidate candidate : candidates) {
            if (candidate.getTimePreference() == null) {
                preferences.merge("NO_PREFERENCE", 1, Integer::sum);
                continue;
            }

            LocalTime prefStart = candidate.getTimePreference().getStartTime();
            LocalTime prefEnd = candidate.getTimePreference().getEndTime();

            for (FixedSlot slot : FixedSlot.values()) {
                if (timesOverlap(slot.getStartTime(), slot.getEndTime(), prefStart, prefEnd)) {
                    preferences.merge(slot.name(), 1, Integer::sum);
                    break;
                }
            }
        }

        return preferences;
    }

    private Map<String, Integer> getTheoryClassesBySlot(List<TheoryClass> classes) {
        Map<String, Integer> bySlot = new HashMap<>();

        for (FixedSlot slot : FixedSlot.values()) {
            bySlot.put(slot.name(), 0);
        }

        for (TheoryClass tc : classes) {
            LocalTime classTime = tc.getStartTime().toLocalTime();
            for (FixedSlot slot : FixedSlot.values()) {
                if (slot.getStartTime().equals(classTime)) {
                    bySlot.merge(slot.name(), 1, Integer::sum);
                    break;
                }
            }
        }

        return bySlot;
    }

    private Double getAverageOccupancy(List<TheoryClass> classes) {
        if (classes.isEmpty()) return 0.0;

        double totalOccupancy = classes.stream()
                .mapToDouble(tc -> (double) tc.getEnrolledStudents() / tc.getCapacity() * 100)
                .average()
                .orElse(0.0);

        return Math.round(totalOccupancy * 100.0) / 100.0;
    }

    private Map<String, Double> getOccupancyBySlot(List<TheoryClass> classes) {
        Map<String, Double> occupancy = new HashMap<>();
        Map<String, List<TheoryClass>> bySlot = new HashMap<>();

        for (FixedSlot slot : FixedSlot.values()) {
            bySlot.put(slot.name(), new ArrayList<>());
        }

        for (TheoryClass tc : classes) {
            LocalTime classTime = tc.getStartTime().toLocalTime();
            for (FixedSlot slot : FixedSlot.values()) {
                if (slot.getStartTime().equals(classTime)) {
                    bySlot.get(slot.name()).add(tc);
                    break;
                }
            }
        }

        for (Map.Entry<String, List<TheoryClass>> entry : bySlot.entrySet()) {
            List<TheoryClass> slotClasses = entry.getValue();
            if (!slotClasses.isEmpty()) {
                double avg = slotClasses.stream()
                        .mapToDouble(tc -> (double) tc.getEnrolledStudents() / tc.getCapacity() * 100)
                        .average()
                        .orElse(0.0);
                occupancy.put(entry.getKey(), Math.round(avg * 100.0) / 100.0);
            } else {
                occupancy.put(entry.getKey(), 0.0);
            }
        }

        return occupancy;
    }

    private Map<String, Double> getAttendanceRateByLesson() {
        List<TheoryLesson> lessons = theoryLessonRepository.findAllByOrderByOrderNumberAsc();
        List<Candidate> candidates = candidateRepository.findAll();
        Map<String, Double> attendanceRate = new HashMap<>();

        for (TheoryLesson lesson : lessons) {
            long attended = candidates.stream()
                    .filter(c -> c.getAttendedLessons().contains(lesson))
                    .count();

            double rate = candidates.isEmpty() ? 0.0 :
                    (double) attended / candidates.size() * 100;

            attendanceRate.put(
                    "Lesson " + lesson.getOrderNumber(),
                    Math.round(rate * 100.0) / 100.0
            );
        }

        return attendanceRate;
    }

    private Map<String, Integer> getPracticalClassesBySlot(List<PracticalClass> classes) {
        Map<String, Integer> bySlot = new HashMap<>();

        for (FixedSlot slot : FixedSlot.values()) {
            bySlot.put(slot.name(), 0);
        }

        for (PracticalClass pc : classes) {
            LocalTime classTime = pc.getStartTime().toLocalTime();
            for (FixedSlot slot : FixedSlot.values()) {
                if (isWithinSlot(classTime, slot)) {
                    bySlot.merge(slot.name(), 1, Integer::sum);
                    break;
                }
            }
        }

        return bySlot;
    }

    private Map<String, Integer> getPracticalClassesByInstructor(List<PracticalClass> classes) {
        Map<String, Integer> byInstructor = new HashMap<>();

        for (PracticalClass pc : classes) {
            if (pc.getInstructor() != null) {
                String instructorName = pc.getInstructor().getName() + " " +
                        pc.getInstructor().getLastname();
                byInstructor.merge(instructorName, 1, Integer::sum);
            }
        }

        return byInstructor;
    }

    private Map<String, Integer> getCompletedPracticalByInstructor() {
        List<PracticalClass> allClasses = practicalClassRepository.findAll();
        Map<String, Integer> completed = new HashMap<>();

        for (PracticalClass pc : allClasses) {
            if (pc.getInstructor() != null && pc.getNotes() != null && !pc.getNotes().isEmpty()) {
                String instructorName = pc.getInstructor().getName() + " " +
                        pc.getInstructor().getLastname();
                completed.merge(instructorName, 1, Integer::sum);
            }
        }

        return completed;
    }

    private Map<String, Integer> getCandidatesByCategory(List<Candidate> candidates) {
        Map<String, Integer> byCategory = new HashMap<>();

        for (Candidate candidate : candidates) {
            if (candidate.getCategory() != null) {
                byCategory.merge(candidate.getCategory().toString(), 1, Integer::sum);
            }
        }

        return byCategory;
    }

    private Map<String, Integer> getCandidatesByStatus(List<Candidate> candidates) {
        Map<String, Integer> byStatus = new HashMap<>();

        for (Candidate candidate : candidates) {
            if (candidate.getStatus() != null) {
                byStatus.merge(candidate.getStatus().toString(), 1, Integer::sum);
            }
        }

        return byStatus;
    }

    private Double getAverageLessonsCompleted(List<Candidate> candidates) {
        if (candidates.isEmpty()) return 0.0;

        double avg = candidates.stream()
                .mapToInt(c -> c.getAttendedLessons().size())
                .average()
                .orElse(0.0);

        return Math.round(avg * 100.0) / 100.0;
    }

    private boolean timesOverlap(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {
        return !(end1.isBefore(start2) || start1.isAfter(end2));
    }

    private boolean isWithinSlot(LocalTime time, FixedSlot slot) {
        return !time.isBefore(slot.getStartTime()) && time.isBefore(slot.getEndTime());
    }
}
