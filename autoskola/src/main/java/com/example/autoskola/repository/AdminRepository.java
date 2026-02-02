package com.example.autoskola.repository;

import com.example.autoskola.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Admin save(Admin admin);
}
