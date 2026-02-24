package com.example.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "permissions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // product:update
    @Column(nullable = false, unique = true)
    private String code;  

    // PRODUCTS, ORDERS
    @Column(nullable = false)
    private String module;   
}
