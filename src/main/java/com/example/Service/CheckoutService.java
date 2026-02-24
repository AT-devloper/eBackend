package com.example.Service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Model.CartItem;
import com.example.Model.Checkout;
import com.example.Repository.CartRepository;
import com.example.Repository.CheckoutRepository;
import com.example.Repository.ProductRepository;

@Service
public class CheckoutService {

    @Autowired
    private CartRepository cartRepo;

    @Autowired
    private CheckoutRepository checkoutRepo;

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private VariantPricingService variantPricingService;

    /**
     * Create checkout items from cart for a user
     */
    @Transactional
    public List<Checkout> createCheckout(Long userId) {

        List<CartItem> cartItems = cartRepo.findByUserId(userId);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // Clear any old checkout for retry safety
        checkoutRepo.deleteByUserId(userId);

        List<Checkout> checkoutItems = cartItems.stream().map(cartItem -> {

            double price = cartItem.getVariantId() != null
                    ? variantPricingService.getPricing(cartItem.getVariantId()).getFinalPrice()
                    : productRepo.findById(cartItem.getProductId())
                                 .orElseThrow(() -> new RuntimeException("Product not found"))
                                 .getPrice();

            Checkout checkout = new Checkout();
            checkout.setUserId(userId);
            checkout.setProductId(cartItem.getProductId());
            checkout.setVariantId(cartItem.getVariantId());
            checkout.setQuantity(cartItem.getQuantity());
            checkout.setPrice(price);
            checkout.setTotalPrice(price * cartItem.getQuantity());
            checkout.setStatus("CREATED");
            checkout.setCreatedAt(LocalDateTime.now());

            return checkout;
        }).toList();

        return checkoutRepo.saveAll(checkoutItems);
    }

    public List<Checkout> getCheckoutByUser(Long userId) {
        return checkoutRepo.findByUserId(userId);
    }

    /**
     * Delete checkout items after order creation
     */
    @Transactional
    public void clearCheckout(Long userId) {
        checkoutRepo.deleteByUserId(userId);
    }
}
