package com.example.autoskola.service;

import com.example.autoskola.dto.CandidateAssignmentDTO;
import com.example.autoskola.dto.InstructorWorkloadDTO;
import com.example.autoskola.model.Candidate;
import com.example.autoskola.model.Instructor;
import com.example.autoskola.model.TrainingStatus;
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

        List<InstructorAssignmentInfo> instructorInfo = new ArrayList<>();

        for (Instructor instructor : instructors) {
            int currentCount = instructor.getCandidates() != null ? instructor.getCandidates().size() : 0;
            int maxCapacity = instructor.getMaxCapacity();
            int freeSpots = maxCapacity - currentCount;

            double priority = calculatePriority(currentCount, maxCapacity);

            instructorInfo.add(new InstructorAssignmentInfo(
                    instructor.getId(),
                    instructor.getName(),
                    instructor.getLastname(),
                    currentCount,
                    maxCapacity,
                    freeSpots,
                    priority
            ));
        }

        List<InstructorAssignmentInfo> availableInstructors = instructorInfo.stream()
                .filter(i -> i.getFreeSpots() > 0)
                .collect(Collectors.toList());

        if (availableInstructors.isEmpty()) {
            throw new RuntimeException("Nema dostupnih instruktora!");
        }

        availableInstructors.sort(Comparator.comparingDouble(InstructorAssignmentInfo::getPriority));

        Map<Long, Long> assignments = new HashMap<>();
        int instructorIndex = 0;

        for (Long candidateId : candidateIds) {
            InstructorAssignmentInfo selected = availableInstructors.get(instructorIndex);

            assignments.put(candidateId, selected.getInstructorId());

            selected.setCurrentCount(selected.getCurrentCount() + 1);
            selected.setFreeSpots(selected.getFreeSpots() - 1);
            selected.setPriority(calculatePriority(selected.getCurrentCount(), selected.getMaxCapacity()));

            if (selected.getFreeSpots() == 0) {
                availableInstructors.remove(instructorIndex);
                if (instructorIndex >= availableInstructors.size()) {
                    instructorIndex = 0;
                }
            } else {
                instructorIndex = (instructorIndex + 1) % availableInstructors.size();
            }

            if (assignments.size() % 5 == 0) {
                availableInstructors.sort(Comparator.comparingDouble(InstructorAssignmentInfo::getPriority));
                instructorIndex = instructorIndex % availableInstructors.size();
            }
        }

        return assignments;
    }

    private double calculatePriority(int currentCount, int maxCapacity) {
        // Priority = (currentCount / maxCapacity) * 100
        if (maxCapacity == 0) return Double.MAX_VALUE;
        return (double) currentCount / maxCapacity * 100;
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
        try {
            List<Candidate> candidates = candidateRepository.findCandidatesForAssignment();

            return candidates.stream()
                    .map(this::mapToCandidateDTO)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            System.err.println("Greška pri pronalaženju kandidata: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private CandidateAssignmentDTO mapToCandidateDTO(Candidate candidate) {
        CandidateAssignmentDTO dto = new CandidateAssignmentDTO();
        dto.setCandidateId(candidate.getId());
        dto.setFirstName(candidate.getName());
        dto.setLastName(candidate.getLastname());
        dto.setManuallyAssigned(false);
        return dto;
    }
}
