package com.example.Controlller;

import com.example.Dto.PermissionDto;
import com.example.Service.RbacService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth/rbac")
@RequiredArgsConstructor
public class RbacController {

    private final RbacService rbacService;

    @PreAuthorize("hasAuthority('role:permission')")
    @GetMapping("/roles/{roleId}/permissions")
    public Map<String, List<PermissionDto>> getPermissions(
            @PathVariable Long roleId
    ) {
        return rbacService.getPermissionsForRole(roleId);
    }

    @PreAuthorize("hasAuthority('role:permission')")
    @PutMapping("/roles/{roleId}/permissions")
    public void updatePermissions(
            @PathVariable Long roleId,
            @RequestBody List<Long> permissionIds
    ) {
        rbacService.updateRolePermissions(roleId, permissionIds);
    }
}
