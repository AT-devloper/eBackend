package com.example.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.Dto.AddToCartRequest;
import com.example.Dto.CartItemResponse;
import com.example.Model.CartItem;
import com.example.Model.Product;
import com.example.Repository.CartRepository;
import com.example.Repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepo;
    private final ProductRepository productRepo;
    private final VariantPricingService variantPricingService;
    private final ProductImageService productImageService;

    // Add or update cart
    public void addToCart(Long userId, AddToCartRequest dto) {

        CartItem item = cartRepo
            .findByUserIdAndProductIdAndVariantId(userId, dto.getProductId(), dto.getVariantId())
            .orElse(new CartItem());

        item.setUserId(userId);
        item.setProductId(dto.getProductId());
        item.setVariantId(dto.getVariantId());

        int qty = dto.getQuantity() == null ? 1 : dto.getQuantity();
        item.setQuantity(item.getQuantity() == null ? qty : item.getQuantity() + qty);

        cartRepo.save(item);
    }

    // Get cart items safely
    public List<CartItemResponse> getCart(Long userId) {
        return cartRepo.findByUserId(userId).stream()
            .filter(ci -> ci.getQuantity() != null && ci.getQuantity() > 0)
            .map(cartItem -> {

                Product product = productRepo.findById(cartItem.getProductId()).orElse(null);
                if (product == null) return null; // skip missing products

                CartItemResponse dto = new CartItemResponse();
                dto.setCartItemId(cartItem.getId());
                dto.setProductId(cartItem.getProductId());
                dto.setVariantId(cartItem.getVariantId());
                dto.setQuantity(cartItem.getQuantity());
                dto.setProductName(product.getName());
                dto.setImage(productImageService.getPrimaryImageUrl(product.getId()));

                double price = cartItem.getVariantId() != null
                    ? variantPricingService.getPricing(cartItem.getVariantId()).getFinalPrice()
                    : (product.getPrice() == null ? 0 : product.getPrice());

                dto.setPrice(price);
                dto.setTotalPrice(price * cartItem.getQuantity());

                return dto;
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    // Remove item safely
    public void removeItem(Long cartItemId, Long userId) {
        Optional<CartItem> itemOpt = cartRepo.findById(cartItemId);

        if (itemOpt.isEmpty()) return;

        CartItem item = itemOpt.get();
        if (!item.getUserId().equals(userId)) return;

        cartRepo.delete(item);
    }
}
