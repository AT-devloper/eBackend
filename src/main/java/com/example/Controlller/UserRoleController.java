package com.example.Controlller;


import com.example.Service.UserRoleService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/rbac/user-roles")
@RequiredArgsConstructor
public class UserRoleController {

    private final UserRoleService userRoleService;
    @PreAuthorize("hasAuthority('role:assign')")
    @PostMapping
    public void assignRole(
            @RequestParam Long userId,
            @RequestParam Long roleId
    ) {
        userRoleService.assignRole(userId, roleId);
    }
}
