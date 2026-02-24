package com.example.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.Dto.ProductImageResponseDto;
import com.example.Dto.WishlistResponse;
import com.example.Model.Product;
import com.example.Model.WishlistItem;
import com.example.Repository.ProductRepository;
import com.example.Repository.WishlistRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository repo;
    private final ProductRepository productRepo;
    private final ProductImageService imageService;
    private final VariantPricingService pricingService;
    private final VariantService variantService;

    // Add an item
    public WishlistItem add(WishlistItem item) {
        return repo.save(item);
    }

    // Remove an item
    public void remove(Long userId, Long productId) {
        repo.findByUserIdAndProductId(userId, productId)
            .ifPresent(repo::delete);
    }

    // Get wishlist
    public List<WishlistResponse> get(Long userId) {
        List<WishlistItem> list = repo.findByUserId(userId);
        if (list.isEmpty()) return List.of();

        List<Long> productIds = list.stream()
                                    .map(WishlistItem::getProductId)
                                    .toList();

        Map<Long, String> imageMap = imageService.getPrimaryImages(productIds)
            .stream()
            .collect(Collectors.toMap(
                ProductImageResponseDto::getProductId,
                ProductImageResponseDto::getImageUrl
            ));

        return list.stream().map(w -> {
            Product p = productRepo.findById(w.getProductId()).orElse(null);

            var variants = variantService.getVariants(w.getProductId());

            Double lowestPrice = variants.stream()
                                         .map(v -> pricingService.getPricing(v.getId()))
                                         .filter(Objects::nonNull)
                                         .map(pr -> pr.getFinalPrice())
                                         .min(Double::compare)
                                         .orElse(p != null && p.getPrice() != null ? p.getPrice() : 0.0);

            WishlistResponse dto = new WishlistResponse();
            dto.setWishlistId(w.getId());
            dto.setProductId(w.getProductId());
            dto.setProductName(p != null ? p.getName() : "Unknown");
            dto.setImage(imageMap.getOrDefault(w.getProductId(), "/placeholder.png"));
            dto.setPrice(lowestPrice);
            dto.setCategory("JEWELRY"); // Default category

            return dto;
        }).toList();
    }
}
