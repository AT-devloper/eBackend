package com.example.Repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Model.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Long> {

	Permission findByCode(String code);
}
