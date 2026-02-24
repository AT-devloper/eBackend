package com.example.Security;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority; 
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.Model.Role;
import com.example.Model.User;
import com.example.Model.UserRole;
import com.example.Repository.RoleRepository;
import com.example.Repository.UserRepository;
import com.example.Repository.UserRoleRepository;
import com.example.Service.PermissionService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    PermissionService permissionService;
    
    @Autowired
    UserRoleRepository userRoleRepo;
    
    @Autowired
    RoleRepository roleRepo ;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();

        // Skip public endpoints
        if (path.startsWith("/auth/") ||
        	    path.equals("/register") ||
        	    path.equals("/verify-email") ||
        	    path.equals("/verify-phone") ||
        	    path.equals("/forgot-password") ||
        	    path.startsWith("/api/brands") ||
        	    path.startsWith("/api/products/page") ||
        	    path.startsWith("/api/variants") ||
        	    path.startsWith("/api/attributes")) {
            filterChain.doFilter(request, response);
            return;
        }

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            if (jwtUtil.isValid(token)) {
                String email = jwtUtil.extractEmail(token);
                User user = userRepository.findByEmail(email).orElse(null);

                if (user != null) {
                	
                	// ✅ NEW PART (RBAC ONLY)
                    Set<String> perms =
                            permissionService.getUserPermissions(user.getId());

                    Set<GrantedAuthority> authorities = new HashSet<>();

                 // Add permissions
                 for (String perm : perms) {
                     authorities.add(new SimpleGrantedAuthority(perm));
                 }

                 // Optionally, add roles as authorities
                 List<UserRole> userRoles = userRoleRepo.findByUserId(user.getId());
                 for (UserRole ur : userRoles) {
                     Role role = roleRepo.findById(ur.getRoleId()).orElseThrow();
                     authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName())); // Spring convention
                 }

                 UsernamePasswordAuthenticationToken authentication =
                         new UsernamePasswordAuthenticationToken(
                                 user,
                                 null,
                                 authorities
                         );

                 SecurityContextHolder.getContext().setAuthentication(authentication);

                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
