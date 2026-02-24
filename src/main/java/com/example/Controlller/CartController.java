package com.example.Controlller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.example.Dto.AddToCartRequest;
import com.example.Dto.CartItemResponse;
import com.example.Model.User;
import com.example.Service.CartService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/add")
    public void addToCart(
        @RequestBody AddToCartRequest dto,
        @AuthenticationPrincipal User user
    ) {
        if (user == null) return; // safe check

        cartService.addToCart(user.getId(), dto);
    }

    @GetMapping
    public List<CartItemResponse> getCart(@AuthenticationPrincipal User user) {
        if (user == null) return List.of(); // safe empty list

        return cartService.getCart(user.getId());
    }

    @DeleteMapping("/remove/{id}")
    public void removeItem(
        @PathVariable Long id,
        @AuthenticationPrincipal User user
    ) {
        if (user == null) return;

        cartService.removeItem(id, user.getId());
    }
}
