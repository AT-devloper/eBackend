package com.example.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "variant_attribute_values")
@Getter
@Setter
@NoArgsConstructor 
@AllArgsConstructor
public class VariantAttributeValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "variant_id", nullable = false)
    private Long variantId;

    @Column(name = "attribute_id", nullable = false)
    private Long attributeId;

    @Column(name = "attribute_value_id", nullable = false)
    private Long attributeValueId;
}
