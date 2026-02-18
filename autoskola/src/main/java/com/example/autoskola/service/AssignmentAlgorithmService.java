package com.example.autoskola.service;

import com.example.autoskola.dto.CandidateAssignmentDTO;
import com.example.autoskola.dto.InstructorWorkloadDTO;
import com.example.autoskola.model.Candidate;
import com.example.autoskola.model.Instructor;
import com.example.autoskola.repository.CandidateRepository;
import com.example.autoskola.repository.InstructorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssignmentAlgorithmService {

    private final InstructorRepository instructorRepository;
    private final CandidateRepository candidateRepository;

    public Map<Long, Long> proposeOptimalAssignment(List<Long> candidateIds) {
        List<Instructor> instructors = instructorRepository.findAll();

        List<InstructorWorkloadDTO> workloads = calculateWorkload(instructors);

        workloads.sort(Comparator.comparingInt(InstructorWorkloadDTO::getCurrentStudentCount));

        Map<Long, Long> assignments = new HashMap<>();

        for (Long candidateId : candidateIds) {
            InstructorWorkloadDTO leastLoaded = workloads.get(0);
            assignments.put(candidateId, leastLoaded.getInstructorId());

            leastLoaded.setCurrentStudentCount(leastLoaded.getCurrentStudentCount() + 1);

            workloads.sort(Comparator.comparingInt(InstructorWorkloadDTO::getCurrentStudentCount));
        }

        return assignments;
    }

    public List<InstructorWorkloadDTO> calculateWorkload(List<Instructor> instructors) {
        List<InstructorWorkloadDTO> workloads = new ArrayList<>();

        for (Instructor instructor : instructors) {
            InstructorWorkloadDTO dto = new InstructorWorkloadDTO();
            dto.setInstructorId(instructor.getId());
            dto.setName(instructor.getName());
            dto.setLastName(instructor.getLastname());

            int currentCount = 0;
            if (instructor.getCandidates() != null) {
                currentCount = instructor.getCandidates().size();
            }
            dto.setCurrentStudentCount(currentCount);

            int maxCapacity = instructor.getMaxCapacity();
            dto.setMaxCapacity(maxCapacity);

            double workload = maxCapacity > 0 ? (double) currentCount / maxCapacity * 100 : 0;
            dto.setWorkloadPercentage(Math.min(workload, 100));

            workloads.add(dto);
        }

        return workloads;
    }

    public List<CandidateAssignmentDTO> getPendingCandidates() {
        List<Candidate> candidates = candidateRepository.findByTheoryCompletedTrueAndInstructorIsNull();

        return candidates.stream()
                .map(this::mapToCandidateDTO)
                .collect(Collectors.toList());
    }

    private CandidateAssignmentDTO mapToCandidateDTO(Candidate candidate) {
        CandidateAssignmentDTO dto = new CandidateAssignmentDTO();
        dto.setCandidateId(candidate.getId());
        dto.setFirstName(candidate.getName());
        dto.setLastName(candidate.getLastname());
        dto.setStatus("PENDING");
        dto.setManuallyAssigned(false);
        return dto;
    }
}
