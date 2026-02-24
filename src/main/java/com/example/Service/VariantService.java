package com.example.Service;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Dto.VariantCreateRequestDto;
import com.example.Dto.VariantResponseDto;
import com.example.Model.Variant;
import com.example.Model.VariantAttributeValue;
import com.example.Repository.VariantAttributeValueRepository;
import com.example.Repository.VariantRepository;

@Service
public class VariantService {

    @Autowired
    private VariantRepository variantRepo;

    @Autowired
    private VariantAttributeValueRepository vavRepo;

    // ----------------------------
    // CREATE VARIANT
    // ----------------------------
    public VariantResponseDto createVariant(Long productId, VariantCreateRequestDto dto) {

        if (productId == null || dto == null) return null;

        Variant variant = new Variant();
        variant.setProductId(productId);
        variant.setSku(dto.getSku());
        variant.setStock(dto.getStock()); // ✅ price REMOVED

        Variant savedVariant = variantRepo.save(variant);

        if (dto.getAttributes() != null && !dto.getAttributes().isEmpty()) {
            for (Map.Entry<Long, Long> entry : dto.getAttributes().entrySet()) {
                if (entry.getKey() == null || entry.getValue() == null) continue;

                VariantAttributeValue vav = new VariantAttributeValue();
                vav.setVariantId(savedVariant.getId());
                vav.setAttributeId(entry.getKey());
                vav.setAttributeValueId(entry.getValue());
                vavRepo.save(vav);
            }
        }

        VariantResponseDto response = new VariantResponseDto();
        response.setId(savedVariant.getId());
        response.setSku(savedVariant.getSku());
        response.setStock(savedVariant.getStock());
        response.setAttributes(dto.getAttributes());

        return response;
    }

    // ----------------------------
    // GET VARIANTS
    // ----------------------------
    public List<VariantResponseDto> getVariants(Long productId) {

        if (productId == null) return Collections.emptyList();

        List<Variant> variants = variantRepo.findByProductId(productId);
        if (variants == null || variants.isEmpty()) return Collections.emptyList();

        List<Long> variantIds = variants.stream()
                                        .map(Variant::getId)
                                        .filter(Objects::nonNull)
                                        .toList();

        List<VariantAttributeValue> allAttrs = variantIds.isEmpty()
                ? Collections.emptyList()
                : vavRepo.findByVariantIdIn(variantIds);

        Map<Long, List<VariantAttributeValue>> attrMap = allAttrs.stream()
                .collect(Collectors.groupingBy(VariantAttributeValue::getVariantId));

        List<VariantResponseDto> response = new ArrayList<>();

        for (Variant v : variants) {

            VariantResponseDto dto = new VariantResponseDto();
            dto.setId(v.getId());
            dto.setSku(v.getSku());
            dto.setStock(v.getStock()); // ✅ price REMOVED

            Map<Long, Long> attrs = new HashMap<>();
            for (VariantAttributeValue vav : attrMap.getOrDefault(v.getId(), Collections.emptyList())) {
                attrs.put(vav.getAttributeId(), vav.getAttributeValueId());
            }

            dto.setAttributes(attrs);
            response.add(dto);
        }

        return response;
    }

    // ----------------------------
    // LOWEST PRICE MAP (OPTIONAL / ADVANCED)
    // ----------------------------
    public Map<Long, Double> findLowestPriceByProductIds(List<Long> productIds) {

        if (productIds == null || productIds.isEmpty())
            return Collections.emptyMap();

        List<Object[]> results = variantRepo.findLowestPriceByProductIds(productIds);

        Map<Long, Double> priceMap = new HashMap<>();
        for (Object[] row : results) {
            priceMap.put((Long) row[0], (Double) row[1]);
        }

        return priceMap;
    }
}
