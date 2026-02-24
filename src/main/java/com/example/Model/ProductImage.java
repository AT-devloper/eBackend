package com.example.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product_images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "variant_id")
    private Long variantId;

    @Column(name = "image_url")   // Maps to image_url in MySQL
    private String imageUrl;     // Matches Cloudinary URL in React

    @Column(name = "is_primary")  // Maps to is_primary in MySQL
    private Boolean isPrimary = false;

    @Column(name = "display_order")
    private Integer displayOrder;
}