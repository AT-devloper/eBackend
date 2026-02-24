package com.example.Controlller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.example.Dto.ProductListItemDto;
import com.example.Dto.ProductPageResponseDto;
import com.example.Service.ProductPageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products/page")
@RequiredArgsConstructor
public class ProductPageController {

    private final ProductPageService service; // Spring injects via constructor

    // LIST ALL PRODUCTS
    @GetMapping
    public List<ProductListItemDto> listProducts() {
        return service.getProductListing();
    }

    // GET FULL PRODUCT PAGE
    @GetMapping("/{productId}/page")
    public ProductPageResponseDto getProductPage(@PathVariable Long productId) {
        return service.getProductPage(productId);
    }
}
