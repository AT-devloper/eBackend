package com.example.Dto;

import java.util.List;
import com.example.Model.Order;
import com.example.Model.OrderItem;
import lombok.Data;

@Data
public class OrderItemsDto {
    private Order order;
    private List<OrderItem> items;
}
