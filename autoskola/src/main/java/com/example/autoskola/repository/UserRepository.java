package com.example.autoskola.repository;

import com.example.autoskola.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    List<User> findByRoleName(String roleName);

    User findByEmail(String email);
    User findByUsername(String username);
}
