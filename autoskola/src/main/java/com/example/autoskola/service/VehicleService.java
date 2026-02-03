package com.example.autoskola.service;

import com.example.autoskola.model.Vehicle;
import com.example.autoskola.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    public Vehicle getById(long id){
        return vehicleRepository.getById(id);
    }
}
