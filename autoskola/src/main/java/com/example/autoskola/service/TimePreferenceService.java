package com.example.autoskola.service;

import com.example.autoskola.model.TimePreference;
import com.example.autoskola.repository.TimePreferenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TimePreferenceService {

    @Autowired
    private TimePreferenceRepository timePreferenceRepository;

    public void save(TimePreference timePreference) {
        timePreferenceRepository.save(timePreference);
    }

}
