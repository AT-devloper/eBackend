package com.example.Service;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Dto.AttributeResponseDto;
import com.example.Dto.ProductAttributeDto;
import com.example.Model.Attribute;
import com.example.Model.AttributeValue;
import com.example.Model.ProductAttribute;
import com.example.Repository.AttributeRepository;
import com.example.Repository.AttributeValueRepository;
import com.example.Repository.ProductAttributeRepository;

@Service
public class ProductAttributeService {

    @Autowired
    private ProductAttributeRepository productAttributeRepo;

    @Autowired
    private AttributeRepository attributeRepo;

    @Autowired
    private AttributeValueRepository valueRepo;

    // ----------------------------
    // SAVE ATTRIBUTES FOR PRODUCT
    // ----------------------------
    public void saveAttributes(Long productId, List<ProductAttributeDto> attributes) {

        // Remove existing attributes for the product
        List<ProductAttribute> existing = productAttributeRepo.findByProductId(productId);
        if (!existing.isEmpty()) {
            productAttributeRepo.deleteAll(existing);
        }

        // Save new attributes
        List<ProductAttribute> toSave = new ArrayList<>();
        for (ProductAttributeDto dto : attributes) {
            if (dto.getValueIds() != null) {
                for (Long valId : dto.getValueIds()) {
                    ProductAttribute pa = new ProductAttribute();
                    pa.setProductId(productId);
                    pa.setAttributeId(dto.getAttributeId());
                    pa.setValueId(valId);
                    toSave.add(pa);
                }
            }
        }
        productAttributeRepo.saveAll(toSave);

    }
    // ----------------------------
    // GET ATTRIBUTES WITH ASSIGNED VALUES
    // ----------------------------
    public List<AttributeResponseDto> getAttributesByProduct(Long productId) {
        List<ProductAttribute> savedAttrs = productAttributeRepo.findByProductId(productId);

        // Map attributeId -> list of valueIds
        Map<Long, List<Long>> assignedMap = savedAttrs.stream()
                .collect(Collectors.groupingBy(
                        ProductAttribute::getAttributeId,
                        Collectors.mapping(ProductAttribute::getValueId, Collectors.toList())
                ));

        List<Attribute> allAttrs = attributeRepo.findAll();
        List<AttributeResponseDto> dtos = new ArrayList<>();

        for (Attribute attr : allAttrs) {
            AttributeResponseDto dto = new AttributeResponseDto();
            dto.setId(attr.getId());
            dto.setName(attr.getName());

            // All possible values
            List<String> values = valueRepo.findByAttributeId(attr.getId())
                                           .stream()
                                           .map(AttributeValue::getValue)
                                           .toList();
            dto.setValues(values);

            // Assigned values (selected for this product)
            List<Long> assignedValueIds = assignedMap.get(attr.getId());
            if (assignedValueIds != null) {
                List<String> assignedValues = assignedValueIds.stream()
                        .map(id -> valueRepo.findById(id).orElse(null))
                        .filter(Objects::nonNull)
                        .map(AttributeValue::getValue)
                        .toList();
                dto.setAssignedValues(assignedValues);
            } else {
                dto.setAssignedValues(Collections.emptyList());
            }

            dtos.add(dto);
        }

        return dtos;
    }
}
