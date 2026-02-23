package com.example.autoskola.repository;

import com.example.autoskola.model.Vehicle;
import com.example.autoskola.model.VehicleStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    boolean existsByRegistrationNumber(String registrationNumber);

    Vehicle getById(long id);

    List<Vehicle> findAllByStatus(VehicleStatus status);

    Optional<Vehicle> findByRegistrationNumber(String registrationNumber);

}
