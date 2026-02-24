package com.example.Service;

import com.example.Dto.PermissionDto;
import com.example.Model.Permission;
import com.example.Model.Role;
import com.example.Model.RolePermission;
import com.example.Model.User;
import com.example.Model.UserRole;
import com.example.Repository.*;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RbacService {

    private final PermissionRepository permissionRepo;
    private final RolePermissionRepository rolePermissionRepo;
    private final RoleRepository roleRepo;
    private final UserRoleRepository userRoleRepo;

    /* ===============================
    GET PERMISSIONS FOR ROLE
    =============================== */
 public Map<String, List<PermissionDto>> getPermissionsForRole(Long roleId) {

     User currentUser = getCurrentUser();

     Role targetRole = roleRepo.findById(roleId)
             .orElseThrow(() -> new RuntimeException("Role not found"));

     if (!canManage(currentUser, targetRole)) {
         throw new AccessDeniedException("Not allowed");
     }

     Set<Long> assigned =
             rolePermissionRepo.findByRoleId(roleId)
                     .stream()
                     .map(RolePermission::getPermissionId)
                     .collect(Collectors.toSet());

     Map<String, List<PermissionDto>> result = new LinkedHashMap<>();

     for (Permission p : permissionRepo.findAll()) {
         result
             .computeIfAbsent(p.getModule(), k -> new ArrayList<>())
             .add(new PermissionDto(
                     p.getId(),
                     p.getCode(),
                     p.getModule(),
                     assigned.contains(p.getId()),
                     false
             ));
     }

     return result;
 }

    /* ===============================
       UPDATE ROLE PERMISSIONS
       =============================== */
    @Transactional
    public void updateRolePermissions(Long roleId, List<Long> permissionIds) {

        User currentUser = getCurrentUser();

        Role targetRole = roleRepo.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        if (!canManage(currentUser, targetRole)) {
            throw new AccessDeniedException("Not allowed");
        }

        rolePermissionRepo.deleteByRoleId(roleId);

        for (Long pid : permissionIds) {
            rolePermissionRepo.save(new RolePermission(null, roleId, pid));
        }
    }

    /* ===============================
       SAFE CURRENT USER FETCH
       =============================== */
    private User getCurrentUser() {

        Authentication auth =
              SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !(auth.getPrincipal() instanceof User)) {
            throw new AccessDeniedException("Unauthenticated");
        }

        return (User) auth.getPrincipal();
    }

    /* ===============================
       HIERARCHY RULE
       =============================== */
    private boolean canManage(User user, Role targetRole) {


        List<UserRole> userRoles =
                userRoleRepo.findByUserId(user.getId());

        Set<String> currentUserRoles = userRoles.stream()
                .map(ur -> roleRepo.findById(ur.getRoleId())
                        .orElseThrow()
                        .getName())
                .collect(Collectors.toSet());

        

        if (currentUserRoles.contains("SUPER_ADMIN")) {
            return true;
        }


        return currentUserRoles.contains("ADMIN")
                && targetRole.getName().equals("SELLER");
    }
}
