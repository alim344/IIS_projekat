package com.example.autoskola.service;

import com.example.autoskola.model.User;
import com.example.autoskola.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    public User getByEmail(String email) {
       return userRepository.findByEmail(email);
    }

    public void update(User user) {
        userRepository.save(user);
    }

}
