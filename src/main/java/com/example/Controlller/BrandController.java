package com.example.Controlller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Dto.BrandRequestDto;
import com.example.Dto.BrandResponseDto;
import com.example.Service.BrandService;

@RestController
@RequestMapping("/api/brands")
public class BrandController {
	@Autowired
    BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @PostMapping("/bulk")
    public String createBrands(@RequestBody List<BrandRequestDto> dtos) {
        brandService.createBrandsBulk(dtos);
        return "Brands created successfully";
    }

    @GetMapping
    public List<BrandResponseDto> getAllBrands() {
        return brandService.getAllBrands();
    }
}
