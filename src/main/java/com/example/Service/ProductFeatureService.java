package com.example.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Model.ProductFeature;
import com.example.Repository.ProductFeatureRepository;

@Service
public class ProductFeatureService {

    @Autowired
    private ProductFeatureRepository repo;

    // Save features for a product (overwrite existing)
    public void saveFeatures(Long productId, List<String> features) {

        // Delete existing features
        repo.deleteByProductId(productId);

        // Save new features
        for (String f : features) {
            ProductFeature pf = new ProductFeature();
            pf.setProductId(productId);
            pf.setFeature(f);
            repo.save(pf);
        }
    }

    // Get all features for a product
    public List<ProductFeature> getFeatures(Long productId) {
        return repo.findByProductId(productId);
    }
}
