package com.kumar.ecomapp.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CartResponseDTO {

    private Long cartId;

    private Long userId;

    private Long productId;

    private String productName;

    private String brand;

    private BigDecimal price;

    private Integer quantity;

    private BigDecimal totalPrice;

}