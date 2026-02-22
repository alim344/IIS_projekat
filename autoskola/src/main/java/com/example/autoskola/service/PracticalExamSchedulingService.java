package com.example.autoskola.service;

import com.example.autoskola.dto.*;
import com.example.autoskola.model.*;
import com.example.autoskola.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PracticalExamSchedulingService {

    private final PracticalExamRepository practicalExamRepository;
    private final CandidateRepository candidateRepository;
    private final InstructorRepository instructorRepository;
    private final ProfessorRepository professorRepository;
    private final ProfessorSchedulingService schedulingService;
    private final InstructorService instructorService;
   // private final NotificationService notificationService;

    public Professor suggestProfessorForExam(LocalDateTime dateTime) {
        return schedulingService.suggestProfessor(dateTime);
    }

    @Transactional
    public PracticalExam scheduleExam(PracticalExamScheduleRequestDTO request) {
        if (request.getDateTime() == null) {
            throw new RuntimeException("Date and time are required");
        }

        Candidate candidate = candidateRepository.findById(request.getCandidateId())
                .orElseThrow(() -> new RuntimeException("Candidate not found"));

        LocalDateTime start = request.getDateTime();
        LocalDateTime end = start.plusHours(1);

        if (!practicalExamRepository
                .findByCandidateIdAndDateTimeBetween(candidate.getId(), start, end)
                .isEmpty()) {
            throw new RuntimeException("Candidate already has an exam at this time");
        }

        Instructor instructor = instructorRepository.findById(request.getInstructorId())
                .orElseThrow(() -> new RuntimeException("Instructor not found"));

        if (!practicalExamRepository
                .findByInstructorIdAndDateTimeBetween(instructor.getId(), start, end)
                .isEmpty()) {
            throw new RuntimeException("Instructor already has an exam at this time");
        }

        Professor professor;
        if (request.getSuggestedProfessorId() != null) {

            professor = professorRepository.findById(request.getSuggestedProfessorId())
                    .orElseThrow(() -> new RuntimeException("Professor not found"));

            if (!schedulingService.getAvailableProfessors(request.getDateTime())
                    .contains(professor)) {
                throw new RuntimeException("Selected professor is not available at this time");
            }
        } else {
            professor = schedulingService.suggestProfessor(request.getDateTime());
        }

        PracticalExam exam = new PracticalExam();
        exam.setDateTime(request.getDateTime());
        exam.setStatus(ExamStatus.SCHEDULED);
        exam.setCandidate(candidate);
        exam.setInstructor(instructor);
        exam.setProfessor(professor);

        PracticalExam savedExam = practicalExamRepository.save(exam);

        // Pošalji notifikacije
        /*sendExamNotifications(savedExam); */

        return savedExam;
    }

    @Transactional
    public PracticalExam changeWitness(Long examId, Long newProfessorId) {
        PracticalExam exam = practicalExamRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found"));

        Professor newProfessor = professorRepository.findById(newProfessorId)
                .orElseThrow(() -> new RuntimeException("Professor not found"));

        LocalDateTime start = exam.getDateTime();
        LocalDateTime end = start.plusHours(1);

        if (!practicalExamRepository
                .findByProfessorIdAndDateTimeBetween(newProfessor.getId(), start, end)
                .isEmpty()) {
            throw new RuntimeException("New professor is not available at this time");
        }

        Professor oldProfessor = exam.getProfessor();

        exam.setProfessor(newProfessor);
        PracticalExam updatedExam = practicalExamRepository.save(exam);

        // Pošalji notifikacije
      /*  notificationService.sendWitnessChangedNotification(oldProfessor, exam);
        notificationService.sendExamScheduledNotification(newProfessor, exam);*/

        return updatedExam;
    }

    /*private void sendExamNotifications(PracticalExam exam) {
        notificationService.sendExamScheduledNotification(exam.getCandidate(), exam);
        notificationService.sendExamScheduledNotification(exam.getInstructor(), exam);
        notificationService.sendExamScheduledNotification(exam.getProfessor(), exam);
    } */

    private PracticalExamResponseDTO mapToDTO(PracticalExam exam) {
        PracticalExamResponseDTO dto = new PracticalExamResponseDTO();
        dto.setId(exam.getId());
        dto.setDateTime(exam.getDateTime());
        dto.setStatus(exam.getStatus().toString());
        dto.setCandidateId(exam.getCandidate().getId());
        dto.setCandidateName(exam.getCandidate().getName());
        dto.setCandidateLastName(exam.getCandidate().getLastname());
        dto.setInstructorId(exam.getInstructor().getId());
        dto.setInstructorName(exam.getInstructor().getName());
        dto.setInstructorLastName(exam.getInstructor().getLastname());
        dto.setProfessorId(exam.getProfessor().getId());
        dto.setProfessorName(exam.getProfessor().getName());
        dto.setProfessorLastName(exam.getProfessor().getLastname());
        return dto;
    }

    public ExamSuggestionDTO suggestBoth(LocalDateTime dateTime, Long candidateId) {
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));

        Instructor instructor = candidate.getInstructor();
        if (instructor == null) {
            throw new RuntimeException("Candidate has no assigned instructor");
        }

        Professor professor = schedulingService.suggestProfessor(dateTime);

        ExamSuggestionDTO result = new ExamSuggestionDTO();

        ProfessorDTO professorDTO = new ProfessorDTO();
        professorDTO.setId(professor.getId());
        professorDTO.setName(professor.getName());
        professorDTO.setLastName(professor.getLastname());
        professorDTO.setEmail(professor.getEmail());
        professorDTO.setUsername(professor.getUsername());
        result.setProfessor(professorDTO);

        InstructorDTO instructorDTO = new InstructorDTO();
        instructorDTO.setId(instructor.getId());
        instructorDTO.setName(instructor.getName());
        instructorDTO.setLastName(instructor.getLastname());
        instructorDTO.setEmail(instructor.getEmail());
        instructorDTO.setUsername(instructor.getUsername());
        result.setInstructor(instructorDTO);

        return result;
    }
}