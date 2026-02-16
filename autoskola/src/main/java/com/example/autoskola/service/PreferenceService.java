package com.example.autoskola.service;

import com.example.autoskola.dto.PreferencesDTO;
import com.example.autoskola.dto.PreferencesUpdateDTO;
import com.example.autoskola.model.Candidate;
import com.example.autoskola.model.TimePreference;
import com.example.autoskola.repository.CandidateRepository;
import com.example.autoskola.repository.TimePreferenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PreferenceService {
    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private TimePreferenceRepository timePreferenceRepository;

    @Autowired
    private CandidateService candidateService;

    public PreferencesDTO getPreferences(Long userId) {
        Candidate candidate = candidateRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidate not found"));

        return new PreferencesDTO(candidate);
    }

    @Transactional
    public PreferencesDTO updatePreferences(Long candidateId, PreferencesUpdateDTO dto) {
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidate not found"));

        if (dto.getPreferredLocation() != null) {
            candidate.setPreferredLocation(dto.getPreferredLocation());
        }

        if (dto.getPreferredDate() != null || dto.getPreferredStartTime() != null || dto.getPreferredEndTime() != null) {
            TimePreference preference = timePreferenceRepository.findByCandidateId(candidateId)
                    .orElse(new TimePreference());

            preference.setCandidate(candidate);

            if (dto.getPreferredDate() != null) {
                preference.setDate(dto.getPreferredDate());
            }
            if (dto.getPreferredStartTime() != null) {
                preference.setStartTime(dto.getPreferredStartTime());
            }
            if (dto.getPreferredEndTime() != null) {
                preference.setEndTime(dto.getPreferredEndTime());
            }

            timePreferenceRepository.save(preference);
        }

        candidateRepository.save(candidate);

        candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidate not found"));

        return new PreferencesDTO(candidate);
    }
}
