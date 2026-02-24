package com.example.Repository;

import com.example.Model.Role;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository
        extends JpaRepository<Role, Long> {
	Optional<Role> findByName(String name);
}