package com.example.Controlller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.example.Dto.WishlistResponse;
import com.example.Model.WishlistItem;
import com.example.Service.WishlistService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    @PostMapping("/add")
    public WishlistItem addToWishlist(@RequestBody WishlistItem item) {
        return wishlistService.add(item);
    }

    @GetMapping("/{userId}")
    public List<WishlistResponse> getWishlist(@PathVariable Long userId) {
        return wishlistService.get(userId);
    }

    @DeleteMapping("/remove")
    public void remove(@RequestParam Long userId, @RequestParam Long productId) {
        wishlistService.remove(userId, productId);
    }
}
