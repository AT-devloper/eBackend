package com.example.Service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Dto.VariantSelectionRequestDto;
import com.example.Dto.VariantSelectionResponseDto;
import com.example.Model.Variant;
import com.example.Model.VariantAttributeValue;
import com.example.Repository.VariantAttributeValueRepository;
import com.example.Repository.VariantRepository;

@Service
public class VariantSelectionService {

    @Autowired
    private VariantRepository variantRepo;

    @Autowired
    private VariantAttributeValueRepository vavRepo;

    public VariantSelectionResponseDto selectVariant(
            Long productId,
            VariantSelectionRequestDto request) {

        List<Variant> variants = variantRepo.findByProductId(productId);

        for (Variant variant : variants) {
            List<VariantAttributeValue> attrs =
                    vavRepo.findByVariantId(variant.getId());

            if (matches(attrs, request.getAttributes())) {
                VariantSelectionResponseDto res =
                        new VariantSelectionResponseDto();

                res.setVariantId(variant.getId());
                res.setPrice(variant.getPrice());
                res.setStock(variant.getStock());
                return res;
            }
        }

        throw new RuntimeException("No matching variant found");
    }

    private boolean matches(
            List<VariantAttributeValue> variantAttrs,
            Map<Long, Long> selectedAttrs) {

        if (variantAttrs.size() != selectedAttrs.size()) {
            return false;
        }

        for (VariantAttributeValue vav : variantAttrs) {
            Long selectedValue =
                    selectedAttrs.get(vav.getAttributeId());

            if (selectedValue == null ||
                !selectedValue.equals(vav.getAttributeValueId())) {
                return false;
            }
        }
        return true;
    }
}

