package com.example.autoskola.service;

import com.example.autoskola.dto.TimePrefInstructorDTO;
import com.example.autoskola.model.Candidate;
import com.example.autoskola.model.TimePreference;
import com.example.autoskola.repository.TimePreferenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TimePreferenceService {

    @Autowired
    private TimePreferenceRepository timePreferenceRepository;
    @Autowired
    private CandidateService candidateService;

    public void save(TimePreference timePreference) {
        timePreferenceRepository.save(timePreference);
    }

    public List<TimePreference> getCandidatesTimePreferencesByIds(Long instructorId) {
        List<Long> ids = candidateService.getIdsByInstructorId(instructorId);
        return timePreferenceRepository.findByCandidate_IdIn(ids);
    }

    public List<TimePrefInstructorDTO> getCandidateTimePreferencesDTO(Long instructorId) {

        List<TimePreference> timePreferences = getCandidatesTimePreferencesByIds(instructorId);

        List<TimePrefInstructorDTO> dtos = new ArrayList<>();
        for (TimePreference pref : timePreferences) {
            Candidate c = pref.getCandidate();
            TimePrefInstructorDTO dto = new TimePrefInstructorDTO();
            dto.setEndTime(pref.getEndTime());
            dto.setStartTime(pref.getStartTime());
            dto.setDate(pref.getDate());
            dto.setCandidate_name(c.getName());
            dto.setCanddiate_lastname(c.getLastname());
            dto.setEmail(c.getEmail());
            dtos.add(dto);
        }
        return dtos;
    }




}
