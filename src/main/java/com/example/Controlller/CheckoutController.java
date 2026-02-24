package com.example.Controlller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Model.Checkout;
import com.example.Service.CheckoutService;

@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {

    @Autowired
    private CheckoutService checkoutService;

    @PostMapping
    public List<Checkout> checkout(
        @AuthenticationPrincipal(expression = "id") Long userId
    ) {
        return checkoutService.createCheckout(userId);
    }

    @GetMapping
    public List<Checkout> myCheckout(
        @AuthenticationPrincipal(expression = "id") Long userId
    ) {
        return checkoutService.getCheckoutByUser(userId);
    }
}
