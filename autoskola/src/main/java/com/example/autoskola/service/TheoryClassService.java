package com.example.autoskola.service;

import com.example.autoskola.dto.*;
import com.example.autoskola.model.*;
import com.example.autoskola.repository.CandidateRepository;
import com.example.autoskola.repository.TheoryClassRepository;
import com.example.autoskola.repository.TheoryLessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TheoryClassService {

    @Autowired
    private TheoryClassRepository theoryClassRepository;

    @Autowired
    private TheoryLessonRepository theoryLessonRepository;

    @Autowired
    private CandidateService candidateService;

    public List<TheoryClass> findAll(){
        return theoryClassRepository.findAll();
    }




    public List<CandidateTheoryClassDTO> getFullScheduleDTO(Candidate candidate) {

        List<TheoryClass> theoryClassList = findAll();

        List<CandidateTheoryClassDTO> dtos = new ArrayList<>();

        for(TheoryClass theoryClass : theoryClassList){
            CandidateTheoryClassDTO dto = new CandidateTheoryClassDTO();
            dto.setId(theoryClass.getId());
            dto.setCapacity(theoryClass.getCapacity());
            dto.setStartTime(theoryClass.getStartTime());
            dto.setEndTime(theoryClass.getEndTime());
            dto.setEnrolledStudents(theoryClass.getEnrolledStudents());
            dto.setEnrolled(isStudentEnrolled(theoryClass,candidate));
            Professor p = theoryClass.getProfessor();
            dto.setProfessorName(p.getName());
            dto.setProfessorLastName(p.getLastname());
            dto.setTitle(theoryClass.getTheoryLesson().getName());
            dtos.add(dto);
        }
        return dtos;

    }


    public List<TheoryClassInfoDTO> getProfessorClasses(Professor professor) {
        List<TheoryClass> tclasses = theoryClassRepository.findByProfessorId(professor.getId());
        return getTheoryClassInfoDTOS(tclasses);
    }

    private static List<TheoryClassInfoDTO> getTheoryClassInfoDTOS(List<TheoryClass> tclasses) {
        List<TheoryClassInfoDTO> dtos = new ArrayList<>();
        for(TheoryClass theoryClass : tclasses){
            TheoryClassInfoDTO dto = new TheoryClassInfoDTO();
            dto.setId(theoryClass.getId());
            dto.setCapacity(theoryClass.getCapacity());
            dto.setStartTime(theoryClass.getStartTime());
            dto.setEndTime(theoryClass.getEndTime());
            dto.setEnrolledStudents(theoryClass.getEnrolledStudents());
            dto.setLessonName(theoryClass.getTheoryLesson().getName());
            dtos.add(dto);

        }
        return dtos;
    }

    public List<TheoryClassInfoDTO> getAllClasses() {
        List<TheoryClass> theoryClassList = theoryClassRepository.findAll();
        return getTheoryClassInfoDTOS(theoryClassList);
    }


    public boolean isStudentEnrolled(TheoryClass theoryClass, Candidate candidate) {
        return theoryClass.getStudents().contains(candidate);
    }

    public TheoryClass findById(Long id) {
        return theoryClassRepository.findById(id).get();
    }

    @Transactional
    public void enroll(Candidate c, long class_id){

        TheoryClass tclass = findById(class_id);

        if(tclass == null){  throw new RuntimeException("Class not found");}

        if(tclass.getEnrolledStudents() >= tclass.getCapacity()){
            throw new RuntimeException("This class is already full");
        }

        if(tclass.getStudents().contains(c)){
            throw new RuntimeException("You are already enrolled in this class");
        }

        TheoryLesson lesson = tclass.getTheoryLesson();
        if(c.getAttendedLessons().contains(lesson)){
            throw new RuntimeException("You already listened to " + lesson.getName());
        }

        if (theoryClassRepository.isCandidateEnrolledInLesson(c.getId(), lesson.getId())) {
            throw new RuntimeException("You are already enrolled in a class for this lesson. Leave the class to join this one!");
        }

        tclass.getStudents().add(c);
        tclass.setEnrolledStudents(tclass.getEnrolledStudents() + 1);

        theoryClassRepository.save(tclass);

    }


    @Transactional
    public void leave(Candidate c, long class_id) {
        TheoryClass tclass = findById(class_id);
        if (tclass == null) {
            throw new RuntimeException("Class not found");
        }

        if (!tclass.getStudents().contains(c)) {
            return;
        }

        tclass.getStudents().remove(c);


        if (tclass.getEnrolledStudents() > 0) {
            tclass.setEnrolledStudents(tclass.getEnrolledStudents() - 1);
        }


        theoryClassRepository.saveAndFlush(tclass);
    }

    public TheoryClassAdminInfoDTO convertToDTO(TheoryClass theoryClass) {
        TheoryClassAdminInfoDTO dto = new TheoryClassAdminInfoDTO();
        dto.setId(theoryClass.getId());
        dto.setStartTime(theoryClass.getStartTime());
        dto.setEndTime(theoryClass.getEndTime());
        dto.setCapacity(theoryClass.getCapacity());
        dto.setEnrolledStudents(theoryClass.getEnrolledStudents());

        if (theoryClass.getTheoryLesson() != null) {
            dto.setTheoryLesson(new TheoryLessonSimpleDTO(theoryClass.getTheoryLesson()));
        }

        if (theoryClass.getProfessor() != null) {
            dto.setProfessor(new ProfessorDTO(theoryClass.getProfessor()));
        }

        if (theoryClass.getStudents() != null) {
            dto.setStudents(theoryClass.getStudents().stream()
                    .map(CandidateSimpleDTO::new)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    public TheoryClassAdminInfoDTO toDTO(TheoryClass tc) {
        TheoryClassAdminInfoDTO dto = new TheoryClassAdminInfoDTO();
        dto.setId(tc.getId());
        dto.setStartTime(tc.getStartTime());
        dto.setEndTime(tc.getEndTime());
        dto.setCapacity(tc.getCapacity());
        dto.setEnrolledStudents(tc.getEnrolledStudents());

        if (tc.getTheoryLesson() != null) {
            dto.setTheoryLesson(new TheoryLessonSimpleDTO(tc.getTheoryLesson())); // Use TheoryLessonSimpleDTO instead
        }

        if (tc.getProfessor() != null) {
            dto.setProfessor(new ProfessorDTO(tc.getProfessor()));
        }

        if (tc.getStudents() != null) {
            dto.setStudents(tc.getStudents().stream()
                    .map(CandidateSimpleDTO::new)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    @Transactional
    public void deleteTheoryClass(Long classId) {
        TheoryClass theoryClass = findById(classId);

        theoryClass.getStudents().clear();
        theoryClass.setProfessor(null);
        theoryClass.setTheoryLesson(null);

        theoryClassRepository.save(theoryClass);

        theoryClassRepository.deleteById(classId);

        System.out.println("Theory class with id " + classId + " deleted successfully");
    }

    @Transactional
    public void submitAttendance(Long classId, List<Long> presentCandidateIds, Long professorId) {
        TheoryClass theoryClass = findById(classId);

        if (!theoryClass.getProfessor().getId().equals(professorId)) {
            throw new RuntimeException("You are not the professor of this class");
        }

        if (theoryClass.getStartTime().isAfter(LocalDateTime.now())) {
            throw new RuntimeException("Class has not started yet");
        }

        TheoryLesson lesson = theoryClass.getTheoryLesson();
        long totalLessons = theoryLessonRepository.count();

        for (Candidate candidate : theoryClass.getStudents()) {
            if (presentCandidateIds.contains(candidate.getId())) {
                boolean alreadyAttended = candidate.getAttendedLessons().stream()
                        .anyMatch(l -> l.getId().equals(lesson.getId()));

                if (!alreadyAttended) {
                    candidate.getAttendedLessons().add(lesson);

                    // da li je odslusao sve lekcije
                    if (candidate.getAttendedLessons().size() >= totalLessons) {
                        candidate.setTheoryCompleted(true);
                        candidate.setStatus(TrainingStatus.PENDING);
                    }

                    candidateService.save(candidate);
                }
            }
        }
    }

    public TheoryClassAdminInfoDTO getClassWithStudents(Long classId, Long professorId) {
        TheoryClass theoryClass = findById(classId);

        if (!theoryClass.getProfessor().getId().equals(professorId)) {
            throw new RuntimeException("You are not the professor of this class");
        }

        return convertToDTO(theoryClass);
    }

    public List<TheoryClass> findByStudentsContainingAndEndTimeBefore(Candidate candidate, LocalDateTime endTime) {
        return theoryClassRepository.findByStudentsContainingAndEndTimeBefore(candidate, endTime);
    }
}
