package com.example.autoskola.service;

import com.example.autoskola.dto.RegistrationDTO;
import com.example.autoskola.model.Admin;
import com.example.autoskola.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Admin save(RegistrationDTO registrationDTO) {

        Admin a = new Admin();

        a.setName(registrationDTO.getFirstName());
        a.setLastname(registrationDTO.getLastName());
        a.setEmail(registrationDTO.getEmail());
        a.setUsername(registrationDTO.getUsername());
        a.setEnabled(true);
        a.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        a.setRole(roleService.findByName("ROLE_ADMIN"));
        return adminRepository.save(a);
    }
}
