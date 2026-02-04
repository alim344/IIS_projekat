package com.example.autoskola.service;

import com.example.autoskola.model.PracticalClass;
import com.example.autoskola.repository.PracticalClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
}
