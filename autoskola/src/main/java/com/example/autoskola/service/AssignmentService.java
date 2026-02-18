package com.example.autoskola.service;

import com.example.autoskola.dto.AssignmentRequestDTO;
import com.example.autoskola.dto.CandidateAssignmentDTO;
import com.example.autoskola.dto.InstructorWorkloadDTO;
import com.example.autoskola.model.Candidate;
import com.example.autoskola.model.Instructor;
import com.example.autoskola.repository.CandidateRepository;
import com.example.autoskola.repository.InstructorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AssignmentService {

    private final CandidateRepository candidateRepository;
    private final InstructorRepository instructorRepository;
    private final AssignmentAlgorithmService algorithmService;

    public Map<Long, Long> proposeAssignment(List<Long> candidateIds) {
        return algorithmService.proposeOptimalAssignment(candidateIds);
    }

    @Transactional
    public void saveAssignment(AssignmentRequestDTO request) {
        Map<Long, Long> finalAssignments;

        if ("OPTIMAL".equals(request.getAlgorithm())) {
            finalAssignments = algorithmService.proposeOptimalAssignment(request.getCandidateIds());
        } else {
            finalAssignments = request.getManualAssignments();
        }

        Map<Long, Integer> instructorLoad = new HashMap<>();

        for (Map.Entry<Long, Long> entry : finalAssignments.entrySet()) {
            Long instructorId = entry.getValue();

            if (!instructorLoad.containsKey(instructorId)) {
                Instructor instructor = instructorRepository.findById(instructorId)
                        .orElseThrow(() -> new RuntimeException("Instructor not found: " + instructorId));

                int currentCount = instructor.getCandidates() != null ? instructor.getCandidates().size() : 0;
                instructorLoad.put(instructorId, currentCount);
            }
        }

        List<String> errors = new ArrayList<>();
        for (Map.Entry<Long, Long> entry : finalAssignments.entrySet()) {
            Long instructorId = entry.getValue();
            Instructor instructor = instructorRepository.findById(instructorId)
                    .orElseThrow(() -> new RuntimeException("Instructor not found: " + instructorId));

            int currentLoad = instructorLoad.get(instructorId);
            int maxCapacity = instructor.getMaxCapacity() != null ? instructor.getMaxCapacity() : 20;

            if (currentLoad + 1 > maxCapacity) {
                errors.add(String.format("%s %s (max: %d, currently: %d)",
                        instructor.getName(), instructor.getLastname(), maxCapacity, currentLoad));
            } else {
                instructorLoad.put(instructorId, currentLoad + 1);
            }
        }

        if (!errors.isEmpty()) {
            throw new RuntimeException("Cannot assign: Instructors at maximum capacity:\n" +
                    String.join("\n", errors));
        }

        for (Map.Entry<Long, Long> entry : finalAssignments.entrySet()) {
            Long candidateId = entry.getKey();
            Long instructorId = entry.getValue();

            Candidate candidate = candidateRepository.findById(candidateId)
                    .orElseThrow(() -> new RuntimeException("Candidate not found: " + candidateId));

            Instructor instructor = instructorRepository.findById(instructorId)
                    .orElseThrow(() -> new RuntimeException("Instructor not found: " + instructorId));

            candidate.setInstructor(instructor);
            candidateRepository.save(candidate);
        }
    }

    public List<CandidateAssignmentDTO> getPendingAssignments() {
        return algorithmService.getPendingCandidates();
    }

    public List<InstructorWorkloadDTO> getInstructorWorkload() {
        List<Instructor> instructors = instructorRepository.findAll();
        return algorithmService.calculateWorkload(instructors);
    }
}
