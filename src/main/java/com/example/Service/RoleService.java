package com.example.Service;

import com.example.Model.Role;
import com.example.Repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RoleService {

 
    private final RoleRepository roleRepo;
    
    public Role createRole(String name) {
        Role role = new Role();
        role.setName(name);
        return roleRepo.save(role);
    }

    public List<Role> getAllRoles() {
        return roleRepo.findAll();
    }
    
    

    
}
