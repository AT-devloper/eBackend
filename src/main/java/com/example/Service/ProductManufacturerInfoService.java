package com.example.Service;

import org.springframework.stereotype.Service;

import com.example.Model.ProductManufacturerInfo;
import com.example.Repository.ProductManufacturerInfoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductManufacturerInfoService {

    private final ProductManufacturerInfoRepository repo;

    public void save(Long productId, String content) {
        ProductManufacturerInfo info = repo.findByProductId(productId)
                .orElse(new ProductManufacturerInfo());
        info.setProductId(productId);
        info.setContent(content);
        repo.save(info);
    }

    public ProductManufacturerInfo get(long productId) {
        return repo.findByProductId(productId).orElse(null);
    }
}
