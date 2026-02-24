package com.example.Controlller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Dto.VariantSelectionRequestDto;
import com.example.Dto.VariantSelectionResponseDto;
import com.example.Service.VariantSelectionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products/{productId}/variants")
@RequiredArgsConstructor
public class VariantSelectionController {

    private final VariantSelectionService service;

    @PostMapping("/select")
    public VariantSelectionResponseDto selectVariant(
        @PathVariable Long productId,
        @RequestBody VariantSelectionRequestDto dto) {
        return service.selectVariant(productId, dto);
    }
}