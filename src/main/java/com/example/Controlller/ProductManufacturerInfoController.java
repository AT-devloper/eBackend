package com.example.Controlller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Model.ProductManufacturerInfo;
import com.example.Service.ProductManufacturerInfoService;


@RestController
@RequestMapping("/api/products/{productId}/manufacturer")
public class ProductManufacturerInfoController {

    @Autowired
    ProductManufacturerInfoService service;
    

     // Save or update "From the Manufacturer" content

    @PostMapping
    public String saveManufacturerInfo(
        @PathVariable Long productId,
        @RequestBody String content) {
        service.save(productId, content);
        return "Manufacturer info saved successfully";
    }
    
    //Get "From the Manufacturer" content

    @GetMapping
    public ProductManufacturerInfo getManufacturerInfo(
        @PathVariable Long productId) {
        return service.get(productId);
    }
}

