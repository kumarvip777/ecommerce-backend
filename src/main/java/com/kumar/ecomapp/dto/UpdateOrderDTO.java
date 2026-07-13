package com.kumar.ecomapp.dto;

import com.kumar.ecomapp.entity.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateOrderDTO {

    @NotNull(message = "Order status is required")
    private OrderStatus status;

    public boolean isEmpty() {
        return status == null;
    }
}