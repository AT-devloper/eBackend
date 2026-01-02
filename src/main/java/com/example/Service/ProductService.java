package com.example.Service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.Dto.ProductRequestDto;
import com.example.Dto.ProductResponseDto;
import com.example.Model.Product;
import com.example.Repository.ProductRepository;

public class ProductService {
	@Autowired
    ProductRepository productRepository;
	@Autowired
    ModelMapper modelMapper;

    // CREATE PRODUCT (BASE CONTAINER)
    public ProductResponseDto createProduct(ProductRequestDto dto) {
        Product product = modelMapper.map(dto, Product.class);
        product.setIsActive(true);
        Product savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct, ProductResponseDto.class);
    }

    // GET PRODUCT BY SLUG (PRODUCT PAGE ENTRY POINT)
    public ProductResponseDto getProductBySlug(String slug) {
        Product product = productRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return modelMapper.map(product, ProductResponseDto.class);
    }
}
