package com.example.autoskola.service;

import com.example.autoskola.dto.EligibleCandidateTheoryDTO;
import com.example.autoskola.dto.RegisterExamDTO;
import com.example.autoskola.dto.TheoryExamDTO;
import com.example.autoskola.dto.TheoryExamResultsDTO;
import com.example.autoskola.model.*;
import com.example.autoskola.repository.CandidateRepository;
import com.example.autoskola.repository.ProfessorRepository;
import com.example.autoskola.repository.TheoryExamRepository;
import com.example.autoskola.repository.TheoryLessonRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TheoryExamService {
    @Autowired
    private TheoryExamRepository theoryExamRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private TheoryLessonRepository theoryLessonRepository;

    private static final int MIN_CANDIDATES_FOR_EXAM = 20;
    @Autowired
    private ScheduledNotificationService scheduledNotificationService;

    public List<EligibleCandidateTheoryDTO> getEligibleCandidates() {
        List<Candidate> candidates = candidateRepository.findByStatus(TrainingStatus.PENDING);

        return candidates.stream().map(EligibleCandidateTheoryDTO::new).collect(Collectors.toList());
    }

    public boolean hasMinimumCandidates() {
        long count = candidateRepository.countByStatus(TrainingStatus.PENDING);

        return count >= MIN_CANDIDATES_FOR_EXAM;
    }

    @Transactional
    public TheoryExamDTO registerForExam(Long professorId, RegisterExamDTO dto) {

        if(dto.getCandidateIds().size() < MIN_CANDIDATES_FOR_EXAM) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Minimum candidates required: " + MIN_CANDIDATES_FOR_EXAM);
        }

        Professor professor = professorRepository.findById(professorId).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Professor not found"));

        List<Candidate> candidates = candidateRepository.findAllById(dto.getCandidateIds());

        List<String> notEligible = candidates.stream()
                .filter(c -> c.getStatus() != TrainingStatus.PENDING)
                .map(c -> c.getName() + " " + c.getLastname())
                .collect(Collectors.toList());

        if(!notEligible.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Following candidates haven't completed theory: " +
                            String.join(", ", notEligible));
        }

        int totalLessons = theoryLessonRepository.findAllByOrderByOrderNumberAsc().size();

        List<String> incomplete = candidates.stream()
                .filter(c -> c.getAttendedLessons().size() < totalLessons)
                .map(c -> c.getName() + " " + c.getLastname() +
                        " (" + c.getAttendedLessons().size() + "/" + totalLessons + " lessons)")
                .collect(Collectors.toList());

        if (!incomplete.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Following candidates haven't attended all lessons: " +
                            String.join(", ", incomplete));
        }

        TheoryExam exam = new TheoryExam();
        exam.setRegistrationDate(LocalDateTime.now());
        exam.setRegisteredBy(professor);
        exam.setCandidates(candidates);
        exam.setStatus(TheoryExamStatus.REQUESTED);

        TheoryExam saved = theoryExamRepository.save(exam);
        return new TheoryExamDTO(saved);
    }

    public List<TheoryExamDTO> getAllExams() {
        return theoryExamRepository.findAll().stream()
                .map(TheoryExamDTO::new)
                .collect(Collectors.toList());
    }

    public List<TheoryExamDTO> getProfessorExams(Long professorId) {
        return theoryExamRepository.findByRegisteredById(professorId).stream()
                .map(TheoryExamDTO::new)
                .collect(Collectors.toList());
    }

    public TheoryExamDTO getExamDetails(Long examId) {
        TheoryExam exam = theoryExamRepository.findById(examId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Exam not found"));

        return new TheoryExamDTO(exam);
    }
    @Transactional
    public void cancelExam(Long examId, Long professorId) {
        TheoryExam exam = theoryExamRepository.findById(examId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Exam not found"));

        if (!exam.getRegisteredBy().getId().equals(professorId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You can only cancel exams you registered");
        }

        if (exam.getStatus() == TheoryExamStatus.COMPLETED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Cannot cancel completed exam");
        }

        exam.setStatus(TheoryExamStatus.CANCELLED);
        theoryExamRepository.save(exam);
    }

    @Transactional
    public TheoryExamDTO setExamDate(Long examId, LocalDate examDate) {

        TheoryExam exam = theoryExamRepository.findById(examId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Exam not found"));

        if(exam.getStatus() == TheoryExamStatus.CANCELLED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot cancelled exam");
        }
        if (exam.getStatus() == TheoryExamStatus.COMPLETED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Cannot modify a completed exam");
        }
        if (examDate == null || examDate.isBefore(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Exam date must be in the future");
        }
        exam.setExamDate(examDate);
        exam.setStatus(TheoryExamStatus.SCHEDULED);
        TheoryExam saved = theoryExamRepository.save(exam);

        // NOTIFIKACIJE KANDIDATIMA
        List<Candidate> candidates = exam.getCandidates();
        scheduledNotificationService.sendTheoryExamNotification(candidates,examDate);


        return new TheoryExamDTO(saved);
    }

    public TheoryExamDTO submitExamResults(Long examId, TheoryExamResultsDTO dto){
        TheoryExam exam = theoryExamRepository.findById(examId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Exam not found"));

        if(exam.getStatus() != TheoryExamStatus.SCHEDULED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Results can only be submitted for SCHEDULED exams");
        }

        if (exam.getExamDate() == null || exam.getExamDate().isAfter(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Exam has not taken place yet");
        }

        List<Long> examCandidateIds = exam.getCandidates().stream()
                .map(c -> c.getId())
                .collect(Collectors.toList());

        List<Long> invalidIds = dto.getPassedCandidateIds().stream()
                .filter(id -> !examCandidateIds.contains(id))
                .collect(Collectors.toList());

        if (!invalidIds.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Some candidate IDs are not part of this exam: " + invalidIds);
        }

        if (dto.getPassedCandidateIds() != null && !dto.getPassedCandidateIds().isEmpty()) {
            List<Candidate> passed = candidateRepository.findAllById(dto.getPassedCandidateIds());
            for (Candidate c : passed) {
                c.setStatus(TrainingStatus.PRACTICAL);
            }
            candidateRepository.saveAll(passed);
        }

        exam.setStatus(TheoryExamStatus.COMPLETED);
        TheoryExam saved = theoryExamRepository.save(exam);

        return new TheoryExamDTO(saved);

    }

}
