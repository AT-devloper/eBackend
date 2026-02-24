package com.example.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "variant_prices")
@Getter
@Setter
@NoArgsConstructor 
@AllArgsConstructor
public class VariantPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long variantId;
    
    private Double mrp;
    private Double sellingPrice;
    
}
