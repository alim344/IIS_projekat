package com.example.autoskola.service;


import com.example.autoskola.model.Role;
import com.example.autoskola.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;


    public Role findById(Long id) {
        Role auth = this.roleRepository.getOne(id);
        return auth;
    }


    public Role findByName(String name) {
        Role role  = this.roleRepository.findByName(name);
        return role;
    }







}
