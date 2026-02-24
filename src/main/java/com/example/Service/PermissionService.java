package com.example.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.example.Model.Permission;
import com.example.Model.RolePermission;
import com.example.Model.UserRole;
import com.example.Repository.PermissionRepository;
import com.example.Repository.RolePermissionRepository;
import com.example.Repository.UserRoleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final UserRoleRepository userRoleRepo;
    private final RolePermissionRepository rolePermRepo;
    private final PermissionRepository permissionRepo;
    
    
    
    public Permission createPermission(String code, String module) {
        Permission p = new Permission();
        p.setCode(code);
        p.setModule(module);
        return permissionRepo.save(p);
    }

    public List<Permission> getAllPermissions() {
        return permissionRepo.findAll();
    }
    
    

    // 🔑 MAIN METHOD USED BY JWT FILTER
    public Set<String> getUserPermissions(Long userId) {

        Set<String> permissions = new HashSet<>();

        List<UserRole> userRoles =
                userRoleRepo.findByUserId(userId);

        for (UserRole ur : userRoles) {

            List<RolePermission> rolePerms =
                    rolePermRepo.findByRoleId(ur.getRoleId());

            for (RolePermission rp : rolePerms) {

                permissionRepo.findById(rp.getPermissionId())
                        .ifPresent(p -> permissions.add(p.getCode()));
            }
        }
        return permissions;
    }
}
