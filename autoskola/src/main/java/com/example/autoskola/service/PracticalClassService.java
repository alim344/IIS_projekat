package com.example.autoskola.service;

import com.example.autoskola.dto.PracticalDTO;
import com.example.autoskola.model.Candidate;
import com.example.autoskola.model.PracticalClass;
import com.example.autoskola.repository.PracticalClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PracticalClassService {

    @Autowired
    private PracticalClassRepository practicalClassRepository;



    public List<PracticalClass> getInstructorNextWeekClasses(long instructorId){

        LocalDate today =  LocalDate.now();
        LocalDate nextMonday = today.with(DayOfWeek.MONDAY).plusWeeks(1);
        LocalDate nextSunday = nextMonday.plusDays(6);


        LocalDateTime startOfNextWeek = nextMonday.atStartOfDay();
        LocalDateTime startOfWeekAfterNext = nextMonday.plusWeeks(1).atStartOfDay();

        return practicalClassRepository.findByInstructorIdAndStartTimeBetween(instructorId, startOfNextWeek, startOfWeekAfterNext);
    }

    public List<PracticalClass> getInstructorThisWeekClasses(long instructorId){

        LocalDate today =  LocalDate.now();
        LocalDate monday = today.with(java.time.temporal.TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate sunday = monday.plusWeeks(1);

        LocalDateTime startOfThisWeek = monday.atStartOfDay();
        LocalDateTime endOfThisWeek = sunday.atStartOfDay();

        return practicalClassRepository.findByInstructorIdAndStartTimeBetween(
                instructorId,
                startOfThisWeek,
                endOfThisWeek
        );
    }


    public List<PracticalClass> getCandidateNextWeekPracticalClasses(long candidate_id){
        LocalDate today =  LocalDate.now();
        LocalDate nextMonday = today.with(DayOfWeek.MONDAY).plusWeeks(1);
        LocalDate nextSunday = nextMonday.plusDays(6);


        LocalDateTime startOfNextWeek = nextMonday.atStartOfDay();
        LocalDateTime startOfWeekAfterNext = nextMonday.plusWeeks(1).atStartOfDay();

        return practicalClassRepository.findByCandidateIdAndStartTimeBetween(candidate_id, startOfNextWeek, startOfWeekAfterNext);

    }

    public List<PracticalClass> getCandidateThisWeekPracticalClasses(long candidate_id){

        LocalDate today =  LocalDate.now();
        LocalDate monday = today.with(java.time.temporal.TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate sunday = monday.plusWeeks(1);

        LocalDateTime startOfThisWeek = monday.atStartOfDay();
        LocalDateTime endOfThisWeek = sunday.atStartOfDay();

        return practicalClassRepository.findByCandidateIdAndStartTimeBetween(
                candidate_id,
                startOfThisWeek,
                endOfThisWeek
        );
    }

    public List<PracticalDTO> getInstructorSchedule(long instructor_id){
        List<PracticalClass> classes =  practicalClassRepository.findByInstructorId(instructor_id);

        List<PracticalDTO> practicalDTOs = new ArrayList<>();


        for(PracticalClass pclass: classes){
            PracticalDTO dto = new PracticalDTO();
            dto.setStartTime(pclass.getStartTime());
            dto.setEndTime(pclass.getEndTime());

            Candidate c = pclass.getCandidate();
            dto.setName(c.getName());
            dto.setLastname(c.getLastname());
            dto.setCategory(c.getCategory().toString());
            dto.setEmail(c.getEmail());
            practicalDTOs.add(dto);
        }
        return practicalDTOs;
    }



}
