package com.example.Model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false) // unique = true
    private String email;

    @Column(nullable = false) 
    private String password;

    @Column(nullable = true) // unique = true
    private String username;

    @Column(nullable = true)  // unique = true
    private String phone;
    
    private boolean emailVerified = false;
    private boolean phoneVerified = false;

    
    private String status = "PENDING";
		
	
}
