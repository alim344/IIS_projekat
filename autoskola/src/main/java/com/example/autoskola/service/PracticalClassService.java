package com.example.autoskola.service;

import com.example.autoskola.dto.CandidatePracticalDTO;
import com.example.autoskola.dto.CandidatePracticalDTO;
import com.example.autoskola.dto.DraftPracticalClassDTO;
import com.example.autoskola.dto.PracticalDTO;
import com.example.autoskola.dto.RecordPracticalDTO;
import com.example.autoskola.model.Candidate;
import com.example.autoskola.model.Instructor;
import com.example.autoskola.model.PracticalClass;
import com.example.autoskola.model.Vehicle;
import com.example.autoskola.repository.PracticalClassRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import org.springframework.web.server.ResponseStatusException;




import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PracticalClassService {

    @Autowired
    private PracticalClassRepository practicalClassRepository;
    @Autowired
    private CandidateService candidateService;
    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private InstructorService instructorService;
    @Autowired
    private ScheduledNotificationService scheduledNotificationService;
    @Autowired
    private CandidateClassRequestService candidateClassRequestService;



    public List<PracticalClass> getInstructorNextWeekClasses(long instructorId) {

        LocalDate today = LocalDate.now();
        LocalDate nextMonday = today.with(DayOfWeek.MONDAY).plusWeeks(1);
        LocalDate nextSunday = nextMonday.plusDays(6);


        LocalDateTime startOfNextWeek = nextMonday.atStartOfDay();
        LocalDateTime startOfWeekAfterNext = nextMonday.plusWeeks(1).atStartOfDay();

        return practicalClassRepository.findByInstructorIdAndStartTimeBetween(instructorId, startOfNextWeek, startOfWeekAfterNext);
    }

    public List<PracticalClass> getInstructorThisWeekClasses(long instructorId) {

        LocalDate today = LocalDate.now();
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


    public List<PracticalClass> getCandidateNextWeekPracticalClasses(long candidate_id) {
        LocalDate today = LocalDate.now();
        LocalDate nextMonday = today.with(DayOfWeek.MONDAY).plusWeeks(1);
        LocalDate nextSunday = nextMonday.plusDays(6);


        LocalDateTime startOfNextWeek = nextMonday.atStartOfDay();
        LocalDateTime startOfWeekAfterNext = nextMonday.plusWeeks(1).atStartOfDay();

        return practicalClassRepository.findByCandidateIdAndStartTimeBetween(candidate_id, startOfNextWeek, startOfWeekAfterNext);

    }

    public List<PracticalClass> getCandidateThisWeekPracticalClasses(long candidate_id) {

        LocalDate today = LocalDate.now();
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


    public List<PracticalDTO> switchToPracticalDTO(List<PracticalClass> practicalClassList) {

        List<PracticalDTO> practicalDTOList = new ArrayList<>();

        for (PracticalClass pclass : practicalClassList) {
            PracticalDTO dto = new PracticalDTO();
            dto.setId(pclass.getId());
            dto.setStartTime(pclass.getStartTime());
            dto.setEndTime(pclass.getEndTime());
            dto.setAccepted(pclass.isAccepted());
            dto.setNote(pclass.getNotes());

            Candidate c = pclass.getCandidate();
            dto.setName(c.getName());
            dto.setLastname(c.getLastname());
            dto.setCategory(c.getCategory().toString());
            dto.setEmail(c.getEmail());
            dto.setPreferredLocation( c.getPreferredLocation());

            practicalDTOList.add(dto);
        }
        return practicalDTOList;

    }

    public List<PracticalDTO> getInstructorSchedule(long instructor_id) {
        List<PracticalClass> classes = practicalClassRepository.findByInstructorId(instructor_id);
        return switchToPracticalDTO(classes);
    }

    public List<PracticalDTO> getCopiedSchedule(long instructor_id) {

        List<PracticalDTO> fullschedule = getInstructorSchedule(instructor_id);
        List<PracticalDTO> nextWeekCopiedSchedule = new ArrayList<>();

        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        LocalDateTime weekStart = startOfWeek.atStartOfDay();
        LocalDateTime weekEnd = endOfWeek.atTime(LocalTime.MAX);

        for (PracticalDTO dto : fullschedule) {

            LocalDateTime start = dto.getStartTime();
            LocalDateTime end = dto.getEndTime();

            if(start.isAfter(weekStart) && end.isBefore(weekEnd)){

                LocalDateTime newStart = start.plusWeeks(1);
                LocalDateTime newEnd = end.plusWeeks(1);

                boolean exists = practicalClassRepository.existsOverlap(instructor_id, newStart, newEnd);

                if(exists){
                    return null;
                }

                PracticalDTO copy = new PracticalDTO();

                copy.setName(dto.getName());
                copy.setLastname(dto.getLastname());
                copy.setCategory(dto.getCategory());
                copy.setEmail(dto.getEmail());
                copy.setStartTime(newStart);
                copy.setEndTime(newEnd);
                copy.setAccepted(false);
                copy.setId(null);
                copy.setPreferredLocation(dto.getPreferredLocation());
                copy.setNote(dto.getNote());

                nextWeekCopiedSchedule.add(copy);

            }

        }

        return nextWeekCopiedSchedule;

    }

    public boolean existsById(long id) {
        return practicalClassRepository.existsById(id);
    }

    public void deleteById(long id) {
        if (!existsById(id)) {
            throw new EntityNotFoundException("Class not found");
        }
        practicalClassRepository.deleteById(id);
    }


    public void deleteClass(long id){
        PracticalClass pc = practicalClassRepository.findById(id);
        LocalDateTime startTime = pc.getStartTime();
        scheduledNotificationService.sendDeletionNotification(startTime,pc.getCandidate());
        deleteById(id);
    }

    public PracticalClass save(PracticalClass practicalClass){
      return  practicalClassRepository.save(practicalClass);

    }

    public PracticalClass findById(long id) {
        return practicalClassRepository.findById(id);
    }

    public PracticalDTO updateDateTime(PracticalDTO dto) {

        PracticalClass pclass = findById(dto.getId());

        if (pclass == null) {
            return null;
        }

        scheduledNotificationService.sendUpdateNotification(pclass,dto);

        pclass.setStartTime(dto.getStartTime());
        pclass.setEndTime(dto.getEndTime());


        practicalClassRepository.save(pclass);
        return dto;

    }

    public List<PracticalClass> saveManualSchedule(List<DraftPracticalClassDTO> dtos, Instructor instructor) {

        List<PracticalClass> newPClasses = new ArrayList<>();

        for (DraftPracticalClassDTO dto : dtos) {
            PracticalClass pc = new PracticalClass();

            pc.setStartTime(dto.getStartTime());
            pc.setEndTime(dto.getEndTime());
            Candidate c = candidateService.findByEmail(dto.getEmail());
            pc.setCandidate(c);
            pc.setAccepted(false);
            pc.setInstructor(instructor);
            scheduledNotificationService.sendCreateNotification(pc.getStartTime(),pc.getCandidate());
            newPClasses.add(pc);

        }
        return practicalClassRepository.saveAll(newPClasses);
    }


    @Transactional
    public DraftPracticalClassDTO saveByDraftDTO(DraftPracticalClassDTO dto, Instructor i, Long requestId){

        PracticalClass pc = new PracticalClass();

        pc.setStartTime(dto.getStartTime());
        pc.setEndTime(dto.getEndTime());
        Candidate c = candidateService.findByEmail(dto.getEmail());
        pc.setCandidate(c);
        pc.setAccepted(false);
        pc.setInstructor(i);
        save(pc);

        if(requestId != null){
            candidateClassRequestService.acceptRequest(requestId);
            scheduledNotificationService.sendRequestAcceptedNotification(pc.getCandidate(),dto.getStartTime());
        }

        scheduledNotificationService.sendCreateNotification(pc.getStartTime(),c);




        return dto;
    }

    public List<CandidatePracticalDTO> getCandidateSchedule(long candidate_id) {
        List<PracticalClass> classes = practicalClassRepository.findByCandidateId(candidate_id);
        return switchToCandidatePracticalDTO(classes);
    }


    public List<CandidatePracticalDTO> switchToCandidatePracticalDTO(List<PracticalClass> pclass){

        List<CandidatePracticalDTO> dtos = new ArrayList<>();

        for(PracticalClass pc: pclass){

            CandidatePracticalDTO dto = new CandidatePracticalDTO();
            dto.setId(pc.getId());
            dto.setStartTime(pc.getStartTime());
            dto.setEndTime(pc.getEndTime());
            Instructor instructor = pc.getInstructor();
            dto.setInstructorName(instructor.getName());
            dto.setInstructorLastName(instructor.getLastname());
            dto.setInstructorEmail(instructor.getEmail());

            dto.setNote(pc.getNotes());
            dto.setPreferredLocation(pc.getCandidate().getPreferredLocation());
            dto.setAccepted(pc.isAccepted());
            dtos.add(dto);
        }

        return dtos;
    }


    public void acceptClass(long class_id){
        PracticalClass practicalClass = practicalClassRepository.findById(class_id);
        practicalClass.setAccepted(true);
        practicalClassRepository.save(practicalClass);
    }


    @Transactional
    public PracticalDTO saveRecordPracticalClass(Long classId, Long instructorId, RecordPracticalDTO dto) {
        PracticalClass pc = practicalClassRepository.findById(classId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Class not found"));

        if (!pc.getInstructor().getId().equals(instructorId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not your class");
        }

        if (pc.getStartTime().isAfter(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Class has not yet started");
        }

        if (dto.getNotes() != null) pc.setNotes(dto.getNotes());

        PracticalClass savedClass = practicalClassRepository.save(pc);

        if (dto.getMileage() != null) {
            Long vehicleId = instructorService.findVehicleIdByInstructorId(instructorId);
            if (vehicleId == null) {
                throw new RuntimeException("Instructor nema vozilo");
            }
            Vehicle vehicle = vehicleService.getById(vehicleId);
            vehicle.setCurrentMileage(dto.getMileage());
            vehicleService.save(vehicle);
        }

        return new PracticalDTO(savedClass.getCandidate(), savedClass);
    }

    public List<PracticalClass> findByCandidateAndAcceptedTrueAndEndTimeBefore(Candidate candidate, LocalDateTime endTime) {
        return practicalClassRepository.findByCandidateAndAcceptedTrueAndEndTimeBefore(candidate, endTime);
    }
}
