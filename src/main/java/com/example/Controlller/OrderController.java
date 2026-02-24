package com.example.Controlller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.example.Dto.OrderItemsDto;
import com.example.Dto.UpdateOrderStatusRequest;
import com.example.Service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // Get all orders of a user
    @GetMapping("/user/{userId}")
    public List<OrderItemsDto> myOrders(@PathVariable Long userId) {
        return orderService.getOrdersWithItems(userId);
    }

    // Get single order by orderNumber (with items)
    @GetMapping("/{orderNumber}")
    public OrderItemsDto trackOrder(@PathVariable String orderNumber) {
        return orderService.getOrderWithItemsByOrderNumber(orderNumber);
    }
    
 // Get all orders - admin
    @GetMapping("/all")
    public List<OrderItemsDto> getAllOrders() {
        return orderService.getAllOrdersWithItems();
    }

    @PutMapping("/{orderId}/status")
    public void updateStatus(
            @PathVariable Long orderId,
            @RequestBody UpdateOrderStatusRequest request
    ) {
        orderService.updateOrderStatus(orderId, request.getStatus());
    }

    @PutMapping("/{orderId}/cancel")
    public void cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
    }

    @PutMapping("/{orderId}/return")
    public void returnOrder(@PathVariable Long orderId) {
        orderService.requestReturn(orderId);
    }
}
