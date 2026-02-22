package com.example.autoskola.service;

import com.example.autoskola.dto.FailPracticalDTO;
import com.example.autoskola.dto.InstructorAnalyticsDTO;
import com.example.autoskola.model.Candidate;
import com.example.autoskola.model.ExamStatus;
import com.example.autoskola.model.PracticalExam;
import com.example.autoskola.model.TrainingStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InstructorAnalytics {

    @Autowired
    private PracticalExamService practicalExamService;
    @Autowired
    private PracticalClassService practicalClassService;


    public int countExams(List<PracticalExam> exams, boolean passed){
        int count = 0;
        if(passed){

            for(PracticalExam exam : exams){
                if(exam.getCandidate().getStatus() == TrainingStatus.PASSED){
                    count++;
                }
            }
        }else{
            for(PracticalExam exam : exams){
                if(exam.getStatus() == ExamStatus.FAILED){
                    count++;
                }
            }
        }
        return count;
    }

    public InstructorAnalyticsDTO getAnalytics(long instructor_id){

        List<PracticalExam> exams = practicalExamService.getByInstructoroId(instructor_id);

        int examCount = exams.size();
        int passedExamCount = countExams(exams,true);
        int failedExamCount = countExams(exams,false);

        double passPercentage = (double) passedExamCount / examCount *100;
        passPercentage = Math.round(passPercentage*100.0)/100.0;

        double failedPercentage = (double) failedExamCount / examCount *100;
        failedPercentage = Math.round(failedPercentage*100.0)/100.0;

        List<FailPracticalDTO> failed = getFailedAnalytics(exams);

        InstructorAnalyticsDTO dto = new InstructorAnalyticsDTO();

        dto.setNumberOfExams(examCount);
        dto.setNumberOfPassedExams(passedExamCount);
        dto.setNumberOfFailedExams(failedExamCount);
        dto.setPassPercentage(passPercentage);
        dto.setFailedPercentage(failedPercentage);
        dto.setFailed(failed);
        return dto;

    }

    public List<FailPracticalDTO> getFailedAnalytics(List<PracticalExam> exams){
        List<FailPracticalDTO> failed = new ArrayList<>();
        for(PracticalExam exam : exams){

            if(exam.getStatus() == ExamStatus.FAILED){

                Candidate c = exam.getCandidate();
                FailPracticalDTO dto  = practicalClassService.getCandidatesPracticalAnalytics(c);

                dto.setCandidate_email(c.getEmail());
                dto.setCandidate_name(c.getName());
                dto.setCandidate_lastName(c.getLastname());

                failed.add(dto);

            }

        }
        return failed;
    }

}
