package com.example.autoskola.service;

import com.example.autoskola.dto.ProfExamDTO;
import com.example.autoskola.model.PracticalExam;
import com.example.autoskola.dto.ProfessorAvailabilityDTO;
import com.example.autoskola.model.Professor;
import com.example.autoskola.repository.PracticalExamRepository;
import com.example.autoskola.repository.ProfessorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class PracticalExamService {

    @Autowired
    private PracticalExamRepository practicalExamRepository;

    public List<PracticalExam> getByInstructoroId(long instructor_id){
        return practicalExamRepository.findByInstructorId(instructor_id);
    }

    @Autowired
    private ProfessorRepository professorRepository;

    private static final int MAX_EXAMS_PER_WEEK = 7;

    public Professor suggestProfessor(LocalDateTime dateTime) {
        List<Professor> allProfessors = professorRepository.findAll();

        if (allProfessors.isEmpty()) {
            throw new RuntimeException("No professors available");
        }

        List<ProfessorAvailabilityDTO> availability = calculateProfessorsAvailability(dateTime);

        List<ProfessorAvailabilityDTO> available = availability.stream()
                .filter(ProfessorAvailabilityDTO::isAvailable)
                .filter(dto -> isProfessorFreeAtTime(dto.getProfessorId(), dateTime))
                .collect(Collectors.toList());

        if (available.isEmpty()) {
            throw new RuntimeException("No professors available at this time");
        }

        available.sort(Comparator.comparingDouble(ProfessorAvailabilityDTO::getWorkloadPercentage));

        ProfessorAvailabilityDTO best = available.get(0);

        return professorRepository.findById(best.getProfessorId())
                .orElseThrow(() -> new RuntimeException("Professor not found"));
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

            boolean isFree = isProfessorFreeAtTime(professor.getId(), dateTime);
            dto.setAvailable(isFree);

            if (!isFree) {
                dto.setReason("Already has an exam at this time");
            } else if (weeklyExams >= MAX_EXAMS_PER_WEEK) {
                dto.setAvailable(false);
                dto.setReason("Maximum weekly exams reached");
            }

            result.add(dto);
        }

        return result;
    }

    private boolean isProfessorFreeAtTime(Long professorId, LocalDateTime dateTime) {
        LocalDateTime start = dateTime;
        LocalDateTime end = dateTime.plusHours(1);

        List<PracticalExam> conflictingExams = practicalExamRepository
                .findByProfessorIdAndDateTimeBetween(professorId, start, end);

        return conflictingExams.isEmpty();
    }

    public List<Professor> getAvailableProfessors(LocalDateTime dateTime) {
        return professorRepository.findAll().stream()
                .filter(p -> isProfessorFreeAtTime(p.getId(), dateTime))
                .collect(Collectors.toList());
    }

    public List<PracticalExam> getProfessorExams(long id) {
        return practicalExamRepository.findByProfessorId(id);
    }

    public List<ProfExamDTO> getExamDTO(long prof_id){
        List<PracticalExam> exams = getProfessorExams(prof_id);
        List<ProfExamDTO> dtos = new ArrayList<>();

        for(PracticalExam exam : exams){
            ProfExamDTO dto = new ProfExamDTO();
            dto.setPractical(true);
            dto.setDate(exam.getDateTime());
            dtos.add(dto);
        }

        return dtos;

    }

}

