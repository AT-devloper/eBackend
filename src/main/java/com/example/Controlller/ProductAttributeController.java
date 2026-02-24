package com.example.Controlller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.Dto.AttributeResponseDto;
import com.example.Dto.ProductAttributeDto;
import com.example.Service.ProductAttributeService;


@RestController
@RequestMapping("/api/products/{productId}/attributes")
public class ProductAttributeController {

    @Autowired
    private ProductAttributeService service;

    @PostMapping
    public String saveAttributes(
            @PathVariable Long productId,
            @RequestBody List<ProductAttributeDto> attributes
    ) {
        service.saveAttributes(productId, attributes);
        return "Attributes saved successfully";
    }


    @GetMapping
    public List<AttributeResponseDto> getAttributes(@PathVariable Long productId) {
        return service.getAttributesByProduct(productId);
    }
}

