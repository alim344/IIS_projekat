package com.example.autoskola.repository;

import com.example.autoskola.model.Instructor;
import com.example.autoskola.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InstructorRepository extends JpaRepository<Instructor, Long> {
    Instructor save(Instructor instructor);

    Instructor findByEmail(String email);

    @Query("SELECT i.vehicle.id FROM Instructor i WHERE i.id = :instructorId")
    Long findVehicleIdByInstructorId(@Param("instructorId") Long instructorId);

}
