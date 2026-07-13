package com.kumar.ecomapp.controller;

import com.kumar.ecomapp.dto.CartRequestDTO;
import com.kumar.ecomapp.dto.CartResponseDTO;
import com.kumar.ecomapp.dto.UpdateCartDTO;
import com.kumar.ecomapp.payload.ApiResponse;
import com.kumar.ecomapp.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Tag(
        name = "Cart Management",
        description = "APIs for managing shopping cart"
)
public class CartController {

    private static final Logger logger =
            LoggerFactory.getLogger(CartController.class);

    private final CartService cartService;

    @Operation(
            summary = "Add Product To Cart",
            description = "Adds a product to user's cart"
    )
    @PostMapping
    public ResponseEntity<ApiResponse<CartResponseDTO>> addToCart(

            @Valid
            @RequestBody CartRequestDTO requestDTO,

            HttpServletRequest request) {

        logger.info(
                "Received request to add product to cart"
        );

        CartResponseDTO responseDTO =
                cartService.addToCart(requestDTO);

        ApiResponse<CartResponseDTO> response =
                ApiResponse.success(
                        responseDTO,
                        "Product added to cart successfully",
                        request.getRequestURI()
                );

        logger.info(
                "Product added to cart successfully"
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(
            summary = "Get Cart By Id",
            description = "Returns cart item using cart id"
    )
    @GetMapping("/{cartId}")
    public ResponseEntity<ApiResponse<CartResponseDTO>> getCartById(

            @PathVariable Long cartId,

            HttpServletRequest request) {

        logger.info(
                "Received request to fetch cart id: {}",
                cartId
        );

        CartResponseDTO responseDTO =
                cartService.getCartById(cartId);

        ApiResponse<CartResponseDTO> response =
                ApiResponse.success(
                        responseDTO,
                        "Cart fetched successfully",
                        request.getRequestURI()
                );

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get User Cart",
            description = "Returns all cart items of a user"
    )
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<CartResponseDTO>>> getUserCart(

            @PathVariable Long userId,

            HttpServletRequest request) {

        logger.info(
                "Received request to fetch cart for user: {}",
                userId
        );

        List<CartResponseDTO> responseDTO =
                cartService.getUserCart(userId);

        ApiResponse<List<CartResponseDTO>> response =
                ApiResponse.success(
                        responseDTO,
                        "Cart fetched successfully",
                        request.getRequestURI()
                );

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Update Cart",
            description = "Updates cart quantity"
    )
    @PutMapping("/{cartId}")
    public ResponseEntity<ApiResponse<CartResponseDTO>> updateCart(

            @PathVariable Long cartId,

            @Valid
            @RequestBody UpdateCartDTO updateDTO,

            HttpServletRequest request) {

        logger.info(
                "Received request to update cart id: {}",
                cartId
        );

        CartResponseDTO responseDTO =
                cartService.updateCart(
                        cartId,
                        updateDTO
                );

        ApiResponse<CartResponseDTO> response =
                ApiResponse.success(
                        responseDTO,
                        "Cart updated successfully",
                        request.getRequestURI()
                );

        logger.info(
                "Cart updated successfully"
        );

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Delete Cart Item",
            description = "Removes a product from cart"
    )
    @DeleteMapping("/{cartId}")
    public ResponseEntity<ApiResponse<String>> deleteCart(

            @PathVariable Long cartId,

            HttpServletRequest request) {

        logger.info(
                "Received request to delete cart id: {}",
                cartId
        );

        cartService.removeCartItem(cartId);

        ApiResponse<String> response =
                ApiResponse.success(
                        "Cart item deleted successfully",
                        "Cart item deleted successfully",
                        request.getRequestURI()
                );

        logger.info(
                "Cart item deleted successfully"
        );

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Clear User Cart",
            description = "Removes all items from user's cart"
    )
    @DeleteMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<String>> clearCart(

            @PathVariable Long userId,

            HttpServletRequest request) {

        logger.info(
                "Received request to clear cart for user: {}",
                userId
        );

        cartService.clearCart(userId);

        ApiResponse<String> response =
                ApiResponse.success(
                        "Cart cleared successfully",
                        "Cart cleared successfully",
                        request.getRequestURI()
                );

        logger.info(
                "Cart cleared successfully"
        );

        return ResponseEntity.ok(response);
    }
}