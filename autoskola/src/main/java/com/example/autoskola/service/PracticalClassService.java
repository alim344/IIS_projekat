package com.example.autoskola.service;

import com.example.autoskola.dto.DraftPracticalClassDTO;
import com.example.autoskola.dto.PracticalDTO;
import com.example.autoskola.model.Candidate;
import com.example.autoskola.model.Instructor;
import com.example.autoskola.model.PracticalClass;
import com.example.autoskola.repository.PracticalClassRepository;
import jakarta.persistence.EntityNotFoundException;
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
    @Autowired
    private CandidateService candidateService;


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


    public List<PracticalDTO> switchToPracticalDTO(List<PracticalClass> practicalClassList){

        List<PracticalDTO> practicalDTOList = new ArrayList<>();

        for(PracticalClass pclass: practicalClassList){
            PracticalDTO dto = new PracticalDTO();
            dto.setId(pclass.getId());
            dto.setStartTime(pclass.getStartTime());
            dto.setEndTime(pclass.getEndTime());
            dto.setAccepted(pclass.isAccepted());

            Candidate c = pclass.getCandidate();
            dto.setName(c.getName());
            dto.setLastname(c.getLastname());
            dto.setCategory(c.getCategory().toString());
            dto.setEmail(c.getEmail());

            practicalDTOList.add(dto);
        }
        return practicalDTOList;

    }

    public List<PracticalDTO> getInstructorSchedule(long instructor_id){
        List<PracticalClass> classes =  practicalClassRepository.findByInstructorId(instructor_id);
        return switchToPracticalDTO(classes);
    }

    public boolean existsById(long id){
       return practicalClassRepository.existsById(id);
    }

    public void deleteById(long id){
        if (!existsById(id)) {
            throw new EntityNotFoundException("Class not found");
        }
        practicalClassRepository.deleteById(id);
    }

    public PracticalClass save(PracticalClass practicalClass){
      return  practicalClassRepository.save(practicalClass);
    }

    public PracticalClass findById(long id){
        return practicalClassRepository.findById(id);
    }

    public PracticalDTO updateDateTime(PracticalDTO dto){

        PracticalClass pclass = findById(dto.getId());

        if(pclass== null){
            return null;
        }

        pclass.setStartTime(dto.getStartTime());
        pclass.setEndTime(dto.getEndTime());

        practicalClassRepository.save(pclass);
        return dto;

    }

    public List<PracticalClass> saveManualSchedule(List<DraftPracticalClassDTO> dtos, Instructor instructor){

        List<PracticalClass> newPClasses = new ArrayList<>();

        for(DraftPracticalClassDTO dto: dtos){
            PracticalClass pc = new PracticalClass();

            pc.setStartTime(dto.getStartTime());
            pc.setEndTime(dto.getEndTime());
            Candidate c = candidateService.findByEmail(dto.getEmail());
            pc.setCandidate(c);
            pc.setAccepted(false);
            pc.setInstructor(instructor);
            newPClasses.add(pc);

        }
       return practicalClassRepository.saveAll(newPClasses);
    }

    public DraftPracticalClassDTO saveByDraftDTO(DraftPracticalClassDTO dto, Instructor i){
        PracticalClass pc = new PracticalClass();

        pc.setStartTime(dto.getStartTime());
        pc.setEndTime(dto.getEndTime());
        Candidate c = candidateService.findByEmail(dto.getEmail());
        pc.setCandidate(c);
        pc.setAccepted(false);
        pc.setInstructor(i);
        save(pc);
        return dto;
    }

    public List<PracticalDTO> getCandidateSchedule(long candidate_id){
        List<PracticalClass> classes =  practicalClassRepository.findByCandidateId(candidate_id);
        return switchToPracticalDTO(classes);
    }


}
