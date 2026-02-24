package com.example.Security;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.Model.Role;
import com.example.Model.User;
import com.example.Model.UserRole;
import com.example.Repository.RoleRepository;
import com.example.Repository.UserRoleRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
    
    private final Key key;
    private final long expiration;
    @Autowired
		 UserRoleRepository userRoleRepo;
    @Autowired
		 RoleRepository roleRepo;
    public JwtUtil(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expiration) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.expiration = expiration;
    }

    // Generate JWT with email as subject + username as claim
    public String generateToken(User user) {
        // For Google users, skip fetching roles
        List<String> roles = new ArrayList<>();
        if (user.getPassword() != null && !user.getPassword().equals("GOOGLE_LOGIN")) {
            List<UserRole> userRoles = userRoleRepo.findByUserId(user.getId());
            for (UserRole ur : userRoles) {
                Role role = roleRepo.findById(ur.getRoleId()).orElse(null);
                if (role != null) roles.add(role.getName());
            }
        }

        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("userId", user.getId())
                .claim("roles", roles)  // empty for Google users
                .claim("username", user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractEmail(String token) {
        return getClaims(token).getSubject();
    }

    public String extractUsername(String token) {
        return getClaims(token).get("username", String.class);
    }

    public boolean isValid(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
