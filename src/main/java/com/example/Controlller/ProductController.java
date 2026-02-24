package com.example.Controlller;

import org.springframework.web.bind.annotation.*;

import com.example.Dto.ProductPageResponseDto;
import com.example.Dto.ProductRequestDto;
import com.example.Dto.ProductResponseDto;
import com.example.Service.ProductPageService;
import com.example.Service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductPageService productPageService;

    // ----------------------------
    // CREATE PRODUCT
    // ----------------------------
    @PostMapping
    public ProductResponseDto createProduct(
            @RequestBody ProductRequestDto dto
    ) {
        return productService.createProduct(dto);
    }

    // ----------------------------
    // PRODUCT PAGE BY SLUG
    // ----------------------------
    @GetMapping("/page/slug/{slug}")
    public ProductPageResponseDto getProductPageBySlug(
            @PathVariable String slug
    ) {
        return productPageService.getProductPageBySlug(slug);
    }
}
