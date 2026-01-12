package com.example.autoskola.service;

import com.example.autoskola.model.User;
import com.example.autoskola.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;



@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    public UserService userService;



    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user =  userService.getByEmail(email) ;
        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with email '%s'.", email));
        } else {
            return user;
        }
    }
}
