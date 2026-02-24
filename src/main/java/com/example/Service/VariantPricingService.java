package com.example.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Dto.VariantDiscountRequestDto;
import com.example.Dto.VariantPriceRequestDto;
import com.example.Dto.VariantPricingResponseDto;
import com.example.Model.DiscountType;
import com.example.Model.VariantDiscount;
import com.example.Model.VariantPrice;
import com.example.Repository.VariantDiscountRepository;
import com.example.Repository.VariantPriceRepository;

@Service
public class VariantPricingService {

    @Autowired
    private VariantPriceRepository priceRepo;

    @Autowired
    private VariantDiscountRepository discountRepo;

    // SET PRICE
    public void setPrice(Long variantId, VariantPriceRequestDto dto) {
        VariantPrice price = priceRepo.findByVariantId(variantId)
                                      .orElse(new VariantPrice());
        price.setVariantId(variantId);
        price.setMrp(dto.getMrp());
        price.setSellingPrice(dto.getSellingPrice());
        priceRepo.save(price);
    }

    // SET DISCOUNT
    public void setDiscount(Long variantId, VariantDiscountRequestDto dto) {
        VariantDiscount discount = discountRepo
                .findByVariantIdAndIsActiveTrue(variantId)
                .orElse(new VariantDiscount());

        discount.setVariantId(variantId);
        discount.setDiscountType(dto.getDiscountType());
        discount.setDiscountValue(dto.getDiscountValue());
        discount.setIsActive(true);

        discountRepo.save(discount);
    }
    
    

    // GET FINAL PRICE
    public VariantPricingResponseDto getPricing(Long variantId) {
        VariantPrice price = priceRepo.findByVariantId(variantId)
                .orElse(null);  // don’t throw

        double sellingPrice = 0.0;
        double mrp = 0.0;

        if (price != null) {
            mrp = price.getMrp() != null ? price.getMrp() : 0.0;
            sellingPrice = price.getSellingPrice() != null ? price.getSellingPrice() : 0.0;
        }

        // calculate discount
        double discountAmount = 0.0;
        var discountOpt = discountRepo.findByVariantIdAndIsActiveTrue(variantId);
        if (discountOpt.isPresent() && sellingPrice > 0) {
            VariantDiscount discount = discountOpt.get();
            if (discount.getDiscountType() == DiscountType.PERCENT) {
                discountAmount = sellingPrice * discount.getDiscountValue() / 100;
            } else {
                discountAmount = discount.getDiscountValue();
            }
        }

        VariantPricingResponseDto res = new VariantPricingResponseDto();
        res.setMrp(mrp);
        res.setSellingPrice(sellingPrice);
        res.setDiscount(discountAmount);
        res.setFinalPrice(Math.max(0, sellingPrice - discountAmount));

        return res;
    }
}
