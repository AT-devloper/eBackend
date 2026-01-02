package com.example.Controlller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Dto.ProductRequestDto;
import com.example.Dto.ProductResponseDto;
import com.example.Service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
	ProductService productService;

    @PostMapping
    public ProductResponseDto createProduct(@RequestBody ProductRequestDto dto) {
        return productService.createProduct(dto);
    }

    @GetMapping("/{slug}")
    public ProductResponseDto getProductBySlug(@PathVariable String slug) {
        return productService.getProductBySlug(slug);
    }
}
