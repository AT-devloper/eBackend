package com.example.Service;

 import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Dto.ProductSpecificationDto;
import com.example.Model.ProductSpecification;
import com.example.Repository.ProductSpecificationRepository;

 @Service
 public class ProductSpecificationService {

	 @Autowired
	 ProductSpecificationRepository repo;

     public void saveBulk(Long productId, List<ProductSpecificationDto> dtos) {
         repo.deleteByProductId(productId);

         for (ProductSpecificationDto dto : dtos) {
             ProductSpecification spec = new ProductSpecification();
             spec.setProductId(productId);
             spec.setSpecKey(dto.getSpecKey());
             spec.setSpecValue(dto.getSpecValue());
             repo.save(spec);
         }
     }

     public List<ProductSpecification> getSpecs(Long productId) {
         return repo.findByProductId(productId);
     }
 }
