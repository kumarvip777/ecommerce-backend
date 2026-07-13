package com.kumar.ecomapp.service;

import com.kumar.ecomapp.dto.OrderRequestDTO;
import com.kumar.ecomapp.dto.OrderResponseDTO;
import com.kumar.ecomapp.dto.UpdateOrderDTO;
import com.kumar.ecomapp.entity.enums.OrderStatus;

import java.util.List;

public interface OrderService {

    // Create Order
    OrderResponseDTO createOrder(
            OrderRequestDTO requestDTO
    );

    // Get Order By Id
    OrderResponseDTO getOrderById(
            Long orderId
    );

    // Get Orders By User Id
    List<OrderResponseDTO> getOrdersByUserId(
            Long userId
    );

    // Get Orders By Status
    List<OrderResponseDTO> getOrdersByStatus(
            OrderStatus status
    );

    // Update Order Status
    OrderResponseDTO updateOrderStatus(
            Long orderId,
            UpdateOrderDTO updateDTO
    );

    // Cancel Order
    void cancelOrder(
            Long orderId
    );

}