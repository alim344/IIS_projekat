package com.example.autoskola.service;

import com.example.autoskola.dto.CandidateTheoryClassDTO;
import com.example.autoskola.model.Candidate;
import com.example.autoskola.model.Professor;
import com.example.autoskola.model.TheoryClass;
import com.example.autoskola.model.TheoryLesson;
import com.example.autoskola.repository.TheoryClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TheoryClassService {

    @Autowired
    private TheoryClassRepository theoryClassRepository;



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

        tclass.getStudents().add(c);
        tclass.setEnrolledStudents(tclass.getEnrolledStudents() + 1);

        theoryClassRepository.save(tclass);

    }


}
