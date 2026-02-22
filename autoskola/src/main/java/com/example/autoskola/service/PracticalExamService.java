package com.example.autoskola.service;

import com.example.autoskola.model.PracticalExam;
import com.example.autoskola.repository.PracticalExamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PracticalExamService {

    @Autowired
    private PracticalExamRepository practicalExamRepository;


    public List<PracticalExam> getByInstructoroId(long instructor_id){
        return practicalExamRepository.findByInstructorId(instructor_id);
    }


}
