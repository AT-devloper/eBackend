package com.example.Controlller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.Dto.VariantCreateRequestDto;
import com.example.Dto.VariantResponseDto;
import com.example.Service.VariantService;

@RestController
@RequestMapping("/api/products/{productId}/variants")
public class VariantController {

    @Autowired
    private VariantService service;

    // ----------------------------
    // CREATE VARIANT
    // ----------------------------
    @PostMapping
    public VariantResponseDto createVariant(
            @PathVariable Long productId,
            @RequestBody VariantCreateRequestDto dto
    ) {
        return service.createVariant(productId, dto);
    }

    // ----------------------------
    // GET VARIANTS
    // ----------------------------
    @GetMapping
    public List<VariantResponseDto> getVariants(
            @PathVariable Long productId
    ) {
        return service.getVariants(productId);
    }
}
