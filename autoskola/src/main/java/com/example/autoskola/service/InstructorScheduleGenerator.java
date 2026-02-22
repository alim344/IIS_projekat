package com.example.autoskola.service;


import com.example.autoskola.dto.CandidatePracticalDTO;
import com.example.autoskola.dto.PracticalDTO;
import com.example.autoskola.dto.TimeRangeDTO;
import com.example.autoskola.model.Candidate;
import com.example.autoskola.model.PracticalClass;
import com.example.autoskola.model.TimePreference;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InstructorScheduleGenerator {


    private final CandidateService candidateService;
    private final PracticalClassService practicalClassService;

    public InstructorScheduleGenerator(CandidateService candidateService, PracticalClassService practicalClassService) {
        this.candidateService = candidateService;
        this.practicalClassService = practicalClassService;
    }

    public List<Candidate> getCandidates(List<String> emails){
        List<Candidate> candidates = new ArrayList<>();
        for(String email : emails){
            Candidate c = candidateService.findByEmail(email);
            candidates.add(c);
        }
        return candidates;
    }


    public boolean checkNextWeekAvailability(long instrictor_id){

        List<PracticalClass> classes = practicalClassService.getInstructorNextWeekClasses(instrictor_id);

        return classes.isEmpty();
    }

    public List<PracticalDTO> generateSchedule(List<LocalDate> lightDays,List<String> emails){

        List<Candidate> candidates = getCandidates(emails);
        List<PracticalDTO> schedule = new ArrayList<>();
        List<TimeRangeDTO> busySlots = new ArrayList<>();




        //prvi cas po preferencama
        for(Candidate c : candidates){
            TimePreference pref = c.getTimePreference();

            if(pref!= null){
                PracticalDTO firstClass = findSlotInRange(c,pref,busySlots,lightDays);
                if(firstClass != null){
                    schedule.add(firstClass);
                    busySlots.add(new TimeRangeDTO(firstClass.getStartTime(), firstClass.getEndTime()));
                }
            }
        }

        //drugi cas po istoriji
        for(Candidate c : candidates){
            LocalTime favTime = getFavoriteTime(c.getId());

            PracticalDTO existingClass = null;
            for(PracticalDTO p : schedule){
                if(p.getEmail().equals(c.getEmail())){
                    existingClass = p;
                    break;
                }
            }

            //ako nisu dobili prvi cas
            PracticalDTO first = existingClass;
            if(first == null){
                first = findSecondSlot(c,favTime,busySlots,lightDays,null);
                if(first != null){
                    schedule.add(first);
                    busySlots.add(new TimeRangeDTO(first.getStartTime(), first.getEndTime()));
                }
            }


            //pravimo drugi cas
            LocalDate forbiddenDate = null;
            if(first != null){
                forbiddenDate = first.getStartTime().toLocalDate();
            }

            PracticalDTO secondClass = findSecondSlot(c,favTime,busySlots,lightDays,forbiddenDate);

            if(secondClass != null){
                schedule.add(secondClass);
                busySlots.add(new TimeRangeDTO(secondClass.getStartTime(), secondClass.getEndTime()));
            }
        }
    return schedule;
    }

    public LocalTime getFavoriteTime(long candidate_id) {

        List<CandidatePracticalDTO> pastClasses = practicalClassService.getCandidateSchedule(candidate_id);
        Map<LocalTime,Integer> counts = new HashMap<>();

        for(CandidatePracticalDTO pc : pastClasses){

            LocalTime time = pc.getStartTime().toLocalTime();
            counts.put(time, counts.getOrDefault(time, 0) + 1);

        }

        LocalTime favTime = null;
        int maxCount = 0;

        for(Map.Entry<LocalTime,Integer> entry : counts.entrySet()){
            if(entry.getValue() > maxCount){
                maxCount = entry.getValue();
                favTime = entry.getKey();
            }
        }

        if(favTime == null){
            return LocalTime.of(10,0);
        }
        return favTime;

    }

    public PracticalDTO findSlotInRange(Candidate c,TimePreference pc, List<TimeRangeDTO> busySlots, List<LocalDate> lightDays) {

        LocalTime startTime = pc.getStartTime();
        LocalTime endTime = pc.getEndTime();
        LocalDate date = pc.getDate();

        LocalDateTime start = LocalDateTime.of(date, startTime);
        LocalDateTime end = LocalDateTime.of(date, endTime);

        while(!start.plusMinutes(90).isAfter(end)){

            if(!isOverlapping(start,start.plusMinutes(90),busySlots)){
                if(!isInstructorOverloaded(date,busySlots,lightDays)){
                    return new PracticalDTO(null,c.getName(),c.getLastname(),c.getCategory().toString(),start,start.plusMinutes(90),c.getEmail(),false,"",c.getPreferredLocation());
                }
            }

            start = start.plusMinutes(30);

        }
        return null;

    }

    public PracticalDTO findSecondSlot(Candidate c,LocalTime favTime, List<TimeRangeDTO> busySlots, List<LocalDate> lightDays, LocalDate excludeDate) {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(TemporalAdjusters.next(DayOfWeek.MONDAY));

        for(int i = 0; i < 7; i++){
            LocalDate date = startOfWeek.plusDays(i);

            if (excludeDate != null) {
                if (date.equals(excludeDate) ||
                        date.equals(excludeDate.plusDays(1)) ||
                        date.equals(excludeDate.minusDays(1))) {
                    continue;
                }
            }

            LocalDateTime start = LocalDateTime.of(date, favTime);
            LocalDateTime end = start.plusMinutes(90);


            if(!isOverlapping(start,end,busySlots) && !isInstructorOverloaded(date,busySlots,lightDays)){
                return new PracticalDTO(null, c.getName(),c.getLastname(),c.getCategory().toString(),start,end,c.getEmail(),false,"",c.getPreferredLocation());
            }



            LocalTime limit = LocalTime.of(20, 0);
            LocalTime currentTry = LocalTime.of(8, 0);

            while (currentTry.isBefore(limit)) {
                if (!currentTry.equals(favTime)) {
                    LocalDateTime begin = LocalDateTime.of(date, currentTry);
                    LocalDateTime theend = start.plusMinutes(90);

                    if (!isOverlapping(start, end, busySlots) && !isInstructorOverloaded(date, busySlots, lightDays)) {
                        return new PracticalDTO(null, c.getName(), c.getLastname(), c.getCategory().toString(), start, end, c.getEmail(), false, "", c.getPreferredLocation());
                    }
                }
                currentTry = currentTry.plusMinutes(30);
            }
        }


        return null;
    }




    public boolean isOverlapping(LocalDateTime start, LocalDateTime newEnd, List<TimeRangeDTO> busySlots) {

        for(TimeRangeDTO t: busySlots){

            if(start.isBefore(t.getEnd()) && newEnd.isAfter(t.getStart())){
                return true;
            }

        }
        return false;
    }

    public boolean isInstructorOverloaded(LocalDate date, List<TimeRangeDTO> busySlots, List<LocalDate> lightDays) {


        if(lightDays.contains(date)){

            for(TimeRangeDTO t: busySlots){
                if(t.getStart().toLocalDate().equals(date)){
                    return true;
                }
            }
        }

        if (isDailyHourLimitReached(date, busySlots)) {
            return true;
        }


        return false;

    }


    private boolean isDailyHourLimitReached(LocalDate date, List<TimeRangeDTO> busySlots) {
        long minutesScheduled = 0;

        for (TimeRangeDTO slot : busySlots) {
            if (slot.getStart().toLocalDate().equals(date)) {
                Duration duration = Duration.between(slot.getStart(), slot.getEnd());
                minutesScheduled += duration.toMinutes();
            }
        }

        //ako dodamo jos ovaj cas ice preci radnih 8 sati dnevno
        return (minutesScheduled + 90) > 480;
    }

/*public List<PracticalDTO> generateSchedule( List<LocalDate> lightDays, List<String> candidate_emails) {

        List<Candidate> candidates = getCandidates(candidate_emails);

        List<PracticalDTO> schedule = new ArrayList<>();
        List<TimeRangeDTO> busySlots = new ArrayList<>();

        for(Candidate c : candidates){

            TimePreference pref = c.getTimePreference();
            LocalTime favTime = getFavoriteTime(c.getId());

            PracticalDTO firstClass = null;
            //prvi cas
            if(pref != null){
               firstClass = findSlotInRange(c,pref,busySlots,lightDays);
            }

            if(firstClass == null){
                firstClass = findSecondSlot(c,favTime,busySlots,lightDays,null);
            }

            if(firstClass != null){
                schedule.add(firstClass);
                busySlots.add(new TimeRangeDTO(firstClass.getStartTime(), firstClass.getEndTime()));
            }

            //drugi cas
            LocalDate forbiddenDate = null;
            if(firstClass != null){
                forbiddenDate = firstClass.getStartTime().toLocalDate();
            }

            PracticalDTO secondClass = findSecondSlot(c,favTime,busySlots,lightDays,forbiddenDate);

            if(secondClass != null){
                schedule.add(secondClass);
                busySlots.add(new TimeRangeDTO(secondClass.getStartTime(), secondClass.getEndTime()));
            }

        }


        return schedule;

    }*/

}
