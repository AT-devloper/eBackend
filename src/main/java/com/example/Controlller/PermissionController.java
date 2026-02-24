package com.example.Controlller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Model.Permission;
import com.example.Service.PermissionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth/rbac/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    // CREATE PERMISSION
    @PostMapping
    @PreAuthorize("hasAuthority('permission:create')")
    public Permission create(@RequestBody Permission permission) {
        return permissionService.createPermission(
                permission.getCode(),
                permission.getModule()
        );
    }

    // LIST PERMISSIONS (for UI)

    @GetMapping
    public List<Permission> list() {
        return permissionService.getAllPermissions();
    }
}
