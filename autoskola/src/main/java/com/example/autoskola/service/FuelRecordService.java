package com.example.autoskola.service;

import com.example.autoskola.repository.FuelRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FuelRecordService {

    @Autowired
    private FuelRecordRepository fuelRecordRepository;
}
