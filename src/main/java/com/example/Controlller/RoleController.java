package com.example.Controlller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Model.Role;
import com.example.Service.RoleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth/rbac/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;
    
    // CREATE ROLE
    @PreAuthorize("hasAuthority('role:create')")
    @PostMapping
    public Role create(@RequestBody Role role) {
        return roleService.createRole(role.getName());
    }

    // LIST ROLES
    @GetMapping
    public List<Role> list() {
        return roleService.getAllRoles();
    }
    


}
