package com.example.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Dto.ProductRequestDto;
import com.example.Dto.ProductResponseDto;
import com.example.Model.Product;
import com.example.Repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ProductImageService imageService;

    private static final Long DEFAULT_CATEGORY_ID = 1L; // Default category ID

    // ----------------------------
    // CREATE PRODUCT
    // ----------------------------
    public ProductResponseDto createProduct(ProductRequestDto dto) {

        Product product = modelMapper.map(dto, Product.class);
        product.setIsActive(true);

        // ✅ Assign default category if none provided
        if (product.getCategoryId() == null) {
            product.setCategoryId(DEFAULT_CATEGORY_ID);
        }

        Product savedProduct = productRepository.save(product);
        return mapToResponse(savedProduct);
    }

    // ----------------------------
    // GET PRODUCT BY SLUG
    // ----------------------------
    public ProductResponseDto getProductBySlug(String slug) {

        Product product = productRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // ✅ Ensure categoryId is valid
        if (product.getCategoryId() == null) {
            product.setCategoryId(DEFAULT_CATEGORY_ID);
        }

        return mapToResponse(product);
    }

    // ----------------------------
    // GET ALL PRODUCTS
    // ----------------------------
    public List<ProductResponseDto> getAllProducts() {

        return productRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ----------------------------
    // PATCH EXISTING PRODUCTS WITHOUT CATEGORY
    // ----------------------------
    public void assignDefaultCategoryToExistingProducts() {
        List<Product> products = productRepository.findAll();
        for (Product p : products) {
            if (p.getCategoryId() == null) {
                p.setCategoryId(DEFAULT_CATEGORY_ID);
                productRepository.save(p);
            }
        }
    }

    // ----------------------------
    // SAFE MAPPER
    // ----------------------------
    private ProductResponseDto mapToResponse(Product product) {

        if (product == null) return null;

        ProductResponseDto dto = new ProductResponseDto();
        dto.setProductId(product.getId());
        dto.setName(Optional.ofNullable(product.getName()).orElse("No Name"));
        dto.setDescription(Optional.ofNullable(product.getDescription()).orElse(""));
        dto.setIsActive(Optional.ofNullable(product.getIsActive()).orElse(false));

        // ✅ Ensure categoryId never null in DTO
        dto.setCategoryId(Optional.ofNullable(product.getCategoryId()).orElse(DEFAULT_CATEGORY_ID));

        dto.setBrandId(product.getBrandId());

        // Images
        List<String> images = Collections.emptyList();
        try {
            images = Optional.ofNullable(imageService.getImages(product.getId(), null))
                    .orElse(Collections.emptyList())
                    .stream()
                    .map(img -> Optional.ofNullable(img.getImageUrl())
                            .orElse("/placeholder.png"))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            images = Collections.emptyList();
        }

        dto.setImages(images);
        return dto;
    }
}
