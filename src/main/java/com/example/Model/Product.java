package com.example.Model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String slug;

    @Column(length = 2000)
    private String description;

    private Long categoryId; 
    private Long brandId;  

    private Boolean isActive = true;
    
    @Column
    private Double price;


    private LocalDateTime createdAt = LocalDateTime.now();
}
