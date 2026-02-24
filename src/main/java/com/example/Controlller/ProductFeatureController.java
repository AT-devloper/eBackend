package com.example.Controlller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.Model.ProductFeature;
import com.example.Service.ProductFeatureService;

@RestController
@RequestMapping("/api/products/{productId}/features")
public class ProductFeatureController {

    @Autowired
    private ProductFeatureService service;

    // SAVE / UPDATE FEATURES (overwrites existing)
    @PostMapping
    public String saveFeatures(
            @PathVariable Long productId,
            @RequestBody List<String> features
    ) {
        service.saveFeatures(productId, features);
        return "Features saved successfully";
    }

    // GET FEATURES
    @GetMapping
    public List<ProductFeature> getFeatures(
            @PathVariable Long productId
    ) {
        return service.getFeatures(productId);
    }
}
