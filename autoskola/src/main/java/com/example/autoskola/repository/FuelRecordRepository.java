package com.example.autoskola.repository;

import com.example.autoskola.model.FuelRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FuelRecordRepository extends JpaRepository<FuelRecord, Long> {
}
