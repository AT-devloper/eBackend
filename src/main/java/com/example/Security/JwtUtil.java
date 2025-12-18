package com.example.Security;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
	
	private final Key key ;
	private final long expiration;
	
	public JwtUtil(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expiration) {

        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.expiration = expiration;
    }

    public String generateToken(String email) {

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
	}
    
	 public String extractEmail(String token) {
		 return getClaims(token).getSubject();
	 }
	 
	 public boolean isValid(String token) {
		 try {
			getClaims(token);
			return true;
		} 
		 catch (Exception e) {
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
