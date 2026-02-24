 package com.example.Controlller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Dto.VariantDiscountRequestDto;
import com.example.Dto.VariantPriceRequestDto;
import com.example.Dto.VariantPricingResponseDto;
import com.example.Service.VariantPricingService;

@RestController
@RequestMapping("/api/variants/{variantId}/pricing")
public class VariantPricingController {

    @Autowired
    private VariantPricingService service;

    // Set or update MRP / selling price
    @PostMapping("/price")
    public String setPrice(
            @PathVariable Long variantId,
            @RequestBody VariantPriceRequestDto dto) {

        service.setPrice(variantId, dto);
        return "Price set";
    }

    // Apply discount
    @PostMapping("/discount")
    public String setDiscount(
            @PathVariable Long variantId,
            @RequestBody VariantDiscountRequestDto dto) {

        service.setDiscount(variantId, dto);
        return "Discount applied";
    }

    // Get pricing details
    @GetMapping
    public VariantPricingResponseDto getPricing(
            @PathVariable Long variantId) {

        return service.getPricing(variantId);
    }
}
