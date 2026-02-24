package com.example.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.Dto.OrderItemsDto;
import com.example.Model.Checkout;
import com.example.Model.Order;
import com.example.Model.OrderItem;
import com.example.Repository.CartRepository;
import com.example.Repository.CheckoutRepository;
import com.example.Repository.OrderItemRepository;
import com.example.Repository.OrderTrackingRepository;
import com.example.util.OrderNumberGenerator;

@Service
public class OrderService {

    @Autowired
    private OrderTrackingRepository orderRepo;

    @Autowired
    private OrderItemRepository orderItemRepo;

    @Autowired
    private CheckoutRepository checkoutRepo;

    @Autowired
    private CartRepository cartRepo;

    // =====================================================
    // CREATE ORDER
    // =====================================================

    @Transactional
    public Order createOrderFromCheckout(Long userId, Long paymentId) {

        List<Checkout> checkoutItems = checkoutRepo.findByUserId(userId);

        if (checkoutItems.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "No checkout data found for user: " + userId
            );
        }

        Order order = new Order();
        order.setOrderNumber(OrderNumberGenerator.generate());
        order.setUserId(userId);
        order.setPaymentId(paymentId);
        order.setOrderStatus("ORDERED");
        order.setPaymentStatus("PAID");

        Order savedOrder = orderRepo.save(order);

        for (Checkout c : checkoutItems) {
            OrderItem item = new OrderItem();
            item.setOrderId(savedOrder.getId());
            item.setUserId(userId);
            item.setProductId(c.getProductId());
            item.setVariantId(c.getVariantId());
            item.setQuantity(c.getQuantity());
            item.setPrice(c.getPrice());
            orderItemRepo.save(item);
        }

        checkoutRepo.deleteByUserId(userId);
        cartRepo.deleteByUserId(userId);

        return savedOrder;
    }

    // =====================================================
    // READ USER ORDERS
    // =====================================================

    public List<OrderItemsDto> getOrdersWithItems(Long userId) {

        List<Order> orders = orderRepo.findByUserId(userId);

        return orders.stream().map(order -> {
            OrderItemsDto dto = new OrderItemsDto();
            dto.setOrder(order);
            dto.setItems(orderItemRepo.findByOrderId(order.getId()));
            return dto;
        }).toList();
    }

    public OrderItemsDto getOrderWithItemsByOrderNumber(String orderNumber) {

        Order order = orderRepo.findByOrderNumber(orderNumber)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        OrderItemsDto dto = new OrderItemsDto();
        dto.setOrder(order);
        dto.setItems(orderItemRepo.findByOrderId(order.getId()));

        return dto;
    }

    // =====================================================
    // ADMIN - GET ALL ORDERS
    // =====================================================

    public List<OrderItemsDto> getAllOrdersWithItems() {

        List<Order> orders = orderRepo.findAll();

        return orders.stream().map(order -> {
            OrderItemsDto dto = new OrderItemsDto();
            dto.setOrder(order);
            dto.setItems(orderItemRepo.findByOrderId(order.getId()));
            return dto;
        }).toList();
    }

    // =====================================================
    // UPDATE STATUS (ADMIN GENERAL CONTROL)
    // =====================================================

    public void updateOrderStatus(Long orderId, String newStatus) {

        Order order = findOrder(orderId);

        order.setOrderStatus(normalize(newStatus));
        orderRepo.save(order);
    }

    // =====================================================
    // ORDER PROGRESSION FLOW (ADMIN)
    // =====================================================

    public void confirmOrder(Long orderId) {
        Order order = findOrder(orderId);
        validateCurrentStatus(order, "ORDERED");
        order.setOrderStatus("CONFIRMED");
        orderRepo.save(order);
    }

    public void packOrder(Long orderId) {
        Order order = findOrder(orderId);
        validateCurrentStatus(order, "CONFIRMED");
        order.setOrderStatus("PACKED");
        orderRepo.save(order);
    }

    public void shipOrder(Long orderId) {
        Order order = findOrder(orderId);
        validateCurrentStatus(order, "PACKED");
        order.setOrderStatus("SHIPPED");
        orderRepo.save(order);
    }

    public void deliverOrder(Long orderId) {
        Order order = findOrder(orderId);
        validateCurrentStatus(order, "SHIPPED");
        order.setOrderStatus("DELIVERED");
        orderRepo.save(order);
    }

    // =====================================================
    // CANCEL ORDER (USER)
    // Allowed only: ORDERED, CONFIRMED, PACKED
    // =====================================================

    public void cancelOrder(Long orderId) {

        Order order = findOrder(orderId);

        String status = normalize(order.getOrderStatus());

        if (!(status.equals("ORDERED") ||
              status.equals("CONFIRMED") ||
              status.equals("PACKED"))) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Order cannot be cancelled at this stage"
            );
        }

        order.setOrderStatus("CANCELLED");
        orderRepo.save(order);
    }

    // =====================================================
    // RETURN FLOW (USER + ADMIN)
    // =====================================================

    // USER → Request Return
    public void requestReturn(Long orderId) {

        Order order = findOrder(orderId);

        validateCurrentStatus(order, "DELIVERED");

        order.setOrderStatus("RETURN_REQUESTED");
        orderRepo.save(order);
    }

    // ADMIN → Approve Return
    public void approveReturn(Long orderId) {

        Order order = findOrder(orderId);

        validateCurrentStatus(order, "RETURN_REQUESTED");

        order.setOrderStatus("RETURN_APPROVED");
        orderRepo.save(order);
    }

    // ADMIN → Pickup Return
    public void pickupReturn(Long orderId) {

        Order order = findOrder(orderId);

        validateCurrentStatus(order, "RETURN_APPROVED");

        order.setOrderStatus("RETURN_PICKED");
        orderRepo.save(order);
    }

    // ADMIN → Complete Return
    public void completeReturn(Long orderId) {

        Order order = findOrder(orderId);

        validateCurrentStatus(order, "RETURN_PICKED");

        order.setOrderStatus("RETURN_COMPLETED");
        orderRepo.save(order);
    }

    // =====================================================
    // PRIVATE HELPERS
    // =====================================================

    private Order findOrder(Long orderId) {
        return orderRepo.findById(orderId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
    }

    private void validateCurrentStatus(Order order, String requiredStatus) {

        String current = normalize(order.getOrderStatus());

        if (!current.equals(requiredStatus)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid order status transition. Current status: " + current
            );
        }
    }

    private String normalize(String status) {
        return status == null ? "" : status.trim().toUpperCase();
    }
}
