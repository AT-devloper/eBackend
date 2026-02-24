package com.example.Service;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.Dto.*;
import com.example.Dto.VariantResponseDto;
import com.example.Model.Product;
import com.example.Model.ProductFeature;
import com.example.Model.ProductSpecification;
import com.example.Repository.BrandRepository;
import com.example.Repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductPageService {

    private final ProductRepository productRepo;
    private final BrandRepository brandRepo;
    private final CategoryService categoryService;
    private final ProductImageService imageService;
    private final VariantService variantService;
    private final ProductSpecificationService specificationService;
    private final ProductFeatureService featureService;
    private final ProductManufacturerInfoService manufacturerService;
    private final ProductAttributeService attributeService;
    private final VariantPricingService variantPricingService;

    // ----------------------------
    // LIST PRODUCTS
    // ----------------------------
    public List<ProductListItemDto> getProductListing() {

        List<Product> products = productRepo.findAll();
        if (products.isEmpty()) return Collections.emptyList();

        List<Long> productIds = products.stream()
                                        .map(Product::getId)
                                        .toList();

        // Brand map
        Map<Long, String> brandMap = brandRepo.findAllById(
                products.stream()
                        .map(Product::getBrandId)
                        .filter(Objects::nonNull)
                        .distinct()
                        .toList()
        ).stream().collect(Collectors.toMap(
                b -> b.getId(),
                b -> b.getName()
        ));

        // Image map
        Map<Long, String> imageMap = imageService.getPrimaryImages(productIds).stream()
                .collect(Collectors.toMap(
                        ProductImageResponseDto::getProductId,
                        img -> img.getImageUrl()
                ));

        List<ProductListItemDto> result = new ArrayList<>();

        for (Product p : products) {

        	// Get all variants
        	List<VariantResponseDto> variants = variantService.getVariants(p.getId());

        	// Attach price to variants (IMPORTANT)
        	List<VariantResponseDto> pricedVariants = new ArrayList<>();
        	if (variants != null) {
        	    for (VariantResponseDto v : variants) {
        	        var pricing = variantPricingService.getPricing(v.getId());
        	        if (pricing != null && pricing.getFinalPrice() > 0) {
        	            v.setPrice(pricing.getFinalPrice());
        	            pricedVariants.add(v);
        	        }
        	    }
        	}

        	// Calculate lowest price
        	double lowestPrice = pricedVariants.stream()
        	        .map(VariantResponseDto::getPrice)
        	        .filter(Objects::nonNull)
        	        .min(Double::compare)
        	        .orElse(0.0);

        	// Build DTO
        	ProductListItemDto dto = new ProductListItemDto();
        	dto.setProductId(p.getId());
        	dto.setName(Optional.ofNullable(p.getName()).orElse("No Name"));
        	dto.setBrand(brandMap.getOrDefault(p.getBrandId(), "Unknown"));
        	dto.setImage(imageMap.getOrDefault(p.getId(), "/placeholder.png"));
        	dto.setPrice(lowestPrice > 0 ? lowestPrice : null);

        	// ✅ THIS IS THE MISSING LINE
        	dto.setVariants(pricedVariants);

        	result.add(dto);
        }

        return result; 
    }

 // ----------------------------
 // PRODUCT PAGE BY SLUG
 // ----------------------------
 public ProductPageResponseDto getProductPageBySlug(String slug) {

     if (slug == null || slug.isBlank()) {
         throw new RuntimeException("Slug cannot be null or empty");
     }

     Product product = productRepo.findBySlug(slug)
             .orElseThrow(() ->
                     new RuntimeException("Product not found with slug: " + slug)
             );

     return getProductPage(product.getId());
 }

    
    // ----------------------------
    // FULL PRODUCT PAGE
    // ----------------------------
    public ProductPageResponseDto getProductPage(Long productId) {

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productId));

        ProductPageResponseDto res = new ProductPageResponseDto();
        res.setProductId(product.getId());
        res.setName(Optional.ofNullable(product.getName()).orElse("No Name"));
        res.setDescription(Optional.ofNullable(product.getDescription()).orElse("No Description"));

        // Brand
        res.setBrandName(Optional.ofNullable(product.getBrandId())
                .flatMap(brandRepo::findById)
                .map(b -> Optional.ofNullable(b.getName()).orElse("Unknown Brand"))
                .orElse("Unknown Brand"));

        // Breadcrumb
        List<CategoryResponseDto> breadcrumb = categoryService.getBreadcrumb(product.getCategoryId());
        if (breadcrumb == null || breadcrumb.isEmpty()) {
            CategoryResponseDto defaultCategory = new CategoryResponseDto();
            defaultCategory.setId(0L);
            defaultCategory.setName("Default Missing Category");
            defaultCategory.setSlug("missing-category");
            breadcrumb = Arrays.asList(defaultCategory);
        }
        res.setBreadcrumb(breadcrumb);

        // Images
        List<ProductImageResponseDto> images = imageService.getImages(productId, null);
        if (images == null || images.isEmpty()) {
            ProductImageResponseDto placeholder = new ProductImageResponseDto();
            placeholder.setImageUrl("/placeholder.png");
            images = Arrays.asList(placeholder);
        }
        res.setImages(images);

        // Variants & Pricing
        List<VariantResponseDto> variants = variantService.getVariants(productId);
        List<VariantResponseDto> finalVariants = new ArrayList<>();
        if (variants != null) {
            for (VariantResponseDto v : variants) {
                var pricing = variantPricingService.getPricing(v.getId());
                if (pricing != null && pricing.getFinalPrice() > 0) {
                    v.setPrice(pricing.getFinalPrice());
                    finalVariants.add(v);
                }
            }
        }
        res.setVariants(finalVariants);

        // Attributes
        List<AttributeResponseDto> attributes = attributeService.getAttributesByProduct(productId);
        res.setAttributes(attributes != null ? attributes : new ArrayList<>());

        // Specifications
        List<ProductSpecification> specifications = specificationService.getSpecs(productId);
        res.setSpecifications(specifications != null ? specifications : new ArrayList<>());

        // Features
        List<ProductFeature> features = featureService.getFeatures(productId);
        res.setFeatures(features != null ? features : new ArrayList<>());

        // Manufacturer Info
        var info = manufacturerService.get(productId);
        if (info == null) {
            info = new com.example.Model.ProductManufacturerInfo();
            info.setProductId(productId);
            info.setContent("N/A");
        }
        res.setManufacturerInfo(info);

        // Set product-level price as lowest variant price
        double lowestPrice = finalVariants.stream()
                .map(VariantResponseDto::getPrice)
                .filter(Objects::nonNull)
                .min(Double::compare)
                .orElse(0.0);
        // Use null if no price
        res.setPrice(lowestPrice > 0 ? lowestPrice : null);

        return res;
    }


}
