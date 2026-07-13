package com.kumar.ecomapp.dto;

import com.kumar.ecomapp.entity.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderResponseDTO {

    private Long orderId;

    private Long userId;

    private Long addressId;

    private BigDecimal totalAmount;

    private OrderStatus status;

    private LocalDateTime orderDate;

    private List<OrderItemResponseDTO> orderItems;

}