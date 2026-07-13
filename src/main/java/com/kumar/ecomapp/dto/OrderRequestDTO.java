package com.kumar.ecomapp.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRequestDTO {

    @NotNull(message = "User id is required")
    private Long userId;

    @NotNull(message = "Address id is required")
    private Long addressId;

}