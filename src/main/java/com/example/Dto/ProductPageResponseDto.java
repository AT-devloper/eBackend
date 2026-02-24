package com.example.Dto;

import java.util.List;

import com.example.Model.ProductFeature;
import com.example.Model.ProductManufacturerInfo;
import com.example.Model.ProductSpecification;

import lombok.Data;

@Data
public class ProductPageResponseDto {
    // BASIC INFO
    private Long productId;
    private String name;
    private String description;
    private String brandName;
    private Double price;                 // LOWEST VARIANT PRICE

    // CATEGORY
    private List<CategoryResponseDto> breadcrumb;

    // IMAGES
    private List<ProductImageResponseDto> images;

    // VARIANTS
    private List<VariantResponseDto> variants;

    // ADD THIS (CRITICAL FOR VARIANT UI)
    private List<AttributeResponseDto> attributes;

    // SPECIFICATIONS
    private List<ProductSpecification> specifications;

    // ABOUT THIS ITEM
    private List<ProductFeature> features;

    // MANUFACTURER INFO
    private ProductManufacturerInfo manufacturerInfo;  

    
    
    // Q&A
//private List<QuestionResponseDto> questions;



// ADDITIONAL INFO
//private List<ProductAdditionalInfo> additionalInfo;

//private List<ProductVideo> videos;

// REVIEWS
//private List<ReviewResponseDto> reviews;
//private RatingSummaryDto ratingSummary;  

}