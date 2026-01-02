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

    private String name;
    private String slug;

    @Column(length = 2000)
    private String description;

    private Long categoryId; // FK → Category
    private Long brandId;    // FK → Brand

    private Boolean isActive = true;

    private LocalDateTime createdAt = LocalDateTime.now();
}
