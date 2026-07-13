package com.kumar.ecomapp.service;

import com.kumar.ecomapp.dto.CartRequestDTO;
import com.kumar.ecomapp.dto.CartResponseDTO;
import com.kumar.ecomapp.dto.UpdateCartDTO;

import java.util.List;

public interface CartService {

    // Add Product To Cart
    CartResponseDTO addToCart(
            CartRequestDTO requestDTO
    );

    // Get Cart By Id
    CartResponseDTO getCartById(
            Long cartId
    );


    // Update Cart Quantity
    CartResponseDTO updateCart(
            Long cartId,
            UpdateCartDTO updateDTO
    );

    // Get User Cart
    List<CartResponseDTO> getUserCart(
            Long userId
    );


    // Remove Product From Cart
    void removeCartItem(
            Long cartId
    );

    // Clear User Cart
    void clearCart(
            Long userId
    );

}