package com.example.autoskola.service;

import com.example.autoskola.dto.ProfessorAvailabilityDTO;
import com.example.autoskola.model.Professor;
import com.example.autoskola.model.PracticalExam;
import com.example.autoskola.repository.ProfessorRepository;
import com.example.autoskola.repository.PracticalExamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfessorSchedulingService {

    private final ProfessorRepository professorRepository;
    private final PracticalExamRepository practicalExamRepository;

    private static final int MAX_EXAMS_PER_WEEK = 10;

    public Professor suggestProfessor(LocalDateTime dateTime) {
        List<Professor> allProfessors = professorRepository.findAll();

        if (allProfessors.isEmpty()) {
            throw new RuntimeException("No professors available");
        }

        LocalDateTime weekStart = dateTime.with(LocalTime.MIN)
                .minusDays(dateTime.getDayOfWeek().getValue() - 1);
        LocalDateTime weekEnd = weekStart.plusDays(7).with(LocalTime.MAX);

        List<Professor> availableNow = new ArrayList<>();
        java.util.Map<Long, Integer> workload = new java.util.HashMap<>();

        for (Professor professor : allProfessors) {
            LocalDateTime start = dateTime;
            LocalDateTime end = dateTime.plusHours(1);

            List<PracticalExam> conflictingExams = practicalExamRepository
                    .findByProfessorIdAndDateTimeBetween(professor.getId(), start, end);

            if (conflictingExams.isEmpty()) {
                availableNow.add(professor);

                int weeklyExams = practicalExamRepository
                        .countByProfessorAndDateTimeBetween(professor, weekStart, weekEnd);
                workload.put(professor.getId(), weeklyExams);
            }
        }

        if (!availableNow.isEmpty()) {
            availableNow.sort(Comparator.comparingInt(p -> workload.get(p.getId())));
            return availableNow.get(0);
        }

        throw new RuntimeException("No professors available at this time");
    }

    public List<ProfessorAvailabilityDTO> calculateProfessorsAvailability(LocalDateTime dateTime) {
        LocalDateTime weekStart = dateTime.with(LocalTime.MIN)
                .minusDays(dateTime.getDayOfWeek().getValue() - 1);
        LocalDateTime weekEnd = weekStart.plusDays(7).with(LocalTime.MAX);

        List<Professor> allProfessors = professorRepository.findAll();
        List<ProfessorAvailabilityDTO> result = new ArrayList<>();

        for (Professor professor : allProfessors) {
            ProfessorAvailabilityDTO dto = new ProfessorAvailabilityDTO();
            dto.setProfessorId(professor.getId());
            dto.setName(professor.getName());
            dto.setLastName(professor.getLastname());

            int weeklyExams = practicalExamRepository
                    .countByProfessorAndDateTimeBetween(professor, weekStart, weekEnd);

            dto.setCurrentExams(weeklyExams);
            dto.setTotalExams(MAX_EXAMS_PER_WEEK);

            double workload = (double) weeklyExams / MAX_EXAMS_PER_WEEK * 100;
            dto.setWorkloadPercentage(Math.min(workload, 100));

            LocalDateTime start = dateTime;
            LocalDateTime end = dateTime.plusHours(1);

            List<PracticalExam> conflictingExams = practicalExamRepository
                    .findByProfessorIdAndDateTimeBetween(professor.getId(), start, end);

            dto.setAvailable(conflictingExams.isEmpty());

            if (!dto.isAvailable()) {
                dto.setReason("Already has an exam at this time");
            } else if (weeklyExams >= MAX_EXAMS_PER_WEEK) {
                dto.setAvailable(false);
                dto.setReason("Maximum weekly exams reached");
            }

            result.add(dto);
        }

        return result;
    }

    public List<Professor> getAvailableProfessors(LocalDateTime dateTime) {
        return professorRepository.findAll().stream()
                .filter(p -> {
                    LocalDateTime start = dateTime;
                    LocalDateTime end = dateTime.plusHours(1);
                    List<PracticalExam> conflicting = practicalExamRepository
                            .findByProfessorIdAndDateTimeBetween(p.getId(), start, end);
                    return conflicting.isEmpty();
                })
                .collect(Collectors.toList());
    }
}