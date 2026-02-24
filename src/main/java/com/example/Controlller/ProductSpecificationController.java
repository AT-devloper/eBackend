package com.example.Controlller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Dto.ProductSpecificationDto;
import com.example.Model.ProductSpecification;
import com.example.Service.ProductSpecificationService;


@RestController
@RequestMapping("/api/products/{productId}/specifications")
public class ProductSpecificationController {
	
	@Autowired
    ProductSpecificationService service;

    @PostMapping("/bulk")
    public String saveSpecs(
        @PathVariable Long productId,
        @RequestBody List<ProductSpecificationDto> dtos) {
        service.saveBulk(productId, dtos);
        return "Specifications saved !";
    }

    @GetMapping
    public List<ProductSpecification> getSpecs(
        @PathVariable Long productId) {
        return service.getSpecs(productId);
    }
}
