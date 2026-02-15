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



    public List<TimePrefInstructorDTO> getCandidateTimePreferencesDTO(Long instructorId) {

        List<Candidate> candidates = candidateService.getByInstructorId(instructorId);

        List<TimePrefInstructorDTO> dtos = new ArrayList<>();
        for (Candidate c : candidates) {
            TimePreference pref = c.getTimePreference();
            TimePrefInstructorDTO dto = new TimePrefInstructorDTO();
            dto.setEndTime(pref.getEndTime());
            dto.setStartTime(pref.getStartTime());
            dto.setDate(pref.getDate());
            dto.setCandidate_name(c.getName());
            dto.setCanddiate_lastname(c.getLastname());
            dto.setEmail(c.getEmail());
            dto.setCategory(c.getCategory().toString());
            dtos.add(dto);
        }
        return dtos;
    }




}
