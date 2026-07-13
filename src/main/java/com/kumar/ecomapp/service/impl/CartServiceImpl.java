package com.kumar.ecomapp.service.impl;

import com.kumar.ecomapp.dto.CartRequestDTO;
import com.kumar.ecomapp.dto.CartResponseDTO;
import com.kumar.ecomapp.dto.UpdateCartDTO;
import com.kumar.ecomapp.entity.Cart;
import com.kumar.ecomapp.entity.Product;
import com.kumar.ecomapp.entity.User;
import com.kumar.ecomapp.entity.enums.ProductStatus;
import com.kumar.ecomapp.exception.cart.CartNotFoundException;
import com.kumar.ecomapp.exception.cart.NoCartItemsFoundException;
import com.kumar.ecomapp.exception.product.ProductNotFoundException;
import com.kumar.ecomapp.exception.user.UserNotFoundException;
import com.kumar.ecomapp.repo.CartRepository;
import com.kumar.ecomapp.repo.ProductRepository;
import com.kumar.ecomapp.repo.UserRepository;
import com.kumar.ecomapp.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private static final Logger logger =
            LoggerFactory.getLogger(CartServiceImpl.class);

    private final CartRepository cartRepository;

    private final UserRepository userRepository;

    private final ProductRepository productRepository;

    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public CartResponseDTO addToCart(
            @Valid CartRequestDTO requestDTO) {

        logger.info(
                "Adding product {} to user {} cart",
                requestDTO.getProductId(),
                requestDTO.getUserId()
        );

        User user = userRepository.findById(
                        requestDTO.getUserId())
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with id: "
                                        + requestDTO.getUserId()));


        Product product = productRepository.findById(
                        requestDTO.getProductId())
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product not found with id: "
                                        + requestDTO.getProductId()));


        validateStock(
                product,
                requestDTO.getQuantity()
        );

        Cart cart = cartRepository
                .findByUserUserIdAndProductProductId(
                        user.getUserId(),
                        product.getProductId())
                .orElse(null);
        if (product.getStatus() != ProductStatus.ACTIVE) {
            throw new IllegalArgumentException(
                    "Product is not available"
            );
        }

        if (cart != null) {

            int newQuantity =
                    cart.getQuantity()
                            + requestDTO.getQuantity();

            validateStock(
                    product,
                    newQuantity
            );
            cart.setQuantity(newQuantity);

        } else {


            cart = new Cart();

            cart.setUser(user);

            cart.setProduct(product);

            cart.setQuantity(
                    requestDTO.getQuantity());
        }

        Cart savedCart =
                cartRepository.save(cart);

        return convertToResponse(savedCart);
    }

    @Override
    public CartResponseDTO getCartById(
            Long cartId) {

        logger.info(
                "Fetching cart id: {}",
                cartId);

        Cart cart =
                cartRepository.findById(cartId)
                        .orElseThrow(() ->
                                new CartNotFoundException(
                                        "Cart not found with id: "
                                                + cartId));

        return convertToResponse(cart);
    }


    @Override
    public List<CartResponseDTO> getUserCart(
            Long userId) {

        userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with id: " + userId
                        ));

        logger.info(
                "Fetching cart for user {}",
                userId);

        List<Cart> carts =
                cartRepository.findByUserUserId(userId);

        if (carts.isEmpty()) {

            throw new NoCartItemsFoundException(
                    "Cart is empty");
        }

        return carts.stream()
                .map(this::convertToResponse)
                .toList();
    }

    private CartResponseDTO convertToResponse(
            Cart cart) {

        CartResponseDTO dto =
                modelMapper.map(
                        cart,
                        CartResponseDTO.class);

        dto.setUserId(
                cart.getUser().getUserId());

        dto.setProductId(
                cart.getProduct().getProductId());

        dto.setProductName(
                cart.getProduct().getProductName());

        dto.setBrand(
                cart.getProduct().getBrand());

        dto.setPrice(
                cart.getProduct().getPrice());

        dto.setTotalPrice(
                cart.getProduct()
                        .getPrice()
                        .multiply(
                                BigDecimal.valueOf(
                                        cart.getQuantity()
                                )
                        )
        );

        return dto;
    }

    @Override
    @Transactional
    public CartResponseDTO updateCart(
            Long cartId,
            @Valid UpdateCartDTO updateDTO) {

        logger.info(
                "Updating cart with id: {}",
                cartId
        );

        if (updateDTO.isEmpty()) {
            throw new IllegalArgumentException(
                    "At least one field must be provided for update"
            );
        }

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() ->
                        new CartNotFoundException(
                                "Cart not found with id: " + cartId
                        ));

        applyQuantityUpdate(cart, updateDTO);

        Cart updatedCart = cartRepository.save(cart);

        logger.info(
                "Cart updated successfully. cartId={}",
                updatedCart.getCartId()
        );

        return convertToResponse(updatedCart);
    }


    @Override
    @Transactional
    public void removeCartItem(
            Long cartId) {

        logger.info(
                "Deleting cart item with id: {}",
                cartId
        );

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() ->
                        new CartNotFoundException(
                                "Cart not found with id: " + cartId
                        ));

        cartRepository.delete(cart);

        logger.info(
                "Cart item deleted successfully. cartId={}",
                cartId
        );
    }

    @Override
    @Transactional
    public void clearCart(
            Long userId) {

        userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with id: " + userId
                        ));

        logger.info(
                "Clearing cart for userId: {}",
                userId
        );

        List<Cart> carts =
                cartRepository.findByUserUserId(userId);

        if (carts.isEmpty()) {
            throw new NoCartItemsFoundException(
                    "Cart is already empty"
            );
        }

        cartRepository.deleteByUserUserId(userId);

        logger.info(
                "Cart cleared successfully for userId={}",
                userId
        );
    }

    private void applyQuantityUpdate(
            Cart cart,
            UpdateCartDTO updateDTO) {

        if (updateDTO.getQuantity() == null) {
            return;
        }

        Product product = cart.getProduct();

        if (product.getStatus() != ProductStatus.ACTIVE) {
            throw new IllegalArgumentException(
                    "Product is not available"
            );
        }

        validateStock(
                product,
                updateDTO.getQuantity()
        );

        cart.setQuantity(updateDTO.getQuantity());
    }

    private void validateStock(
            Product product,
            int quantity) {

        if (quantity > product.getStockQuantity()) {
            throw new IllegalArgumentException(
                    "Requested quantity exceeds available stock"
            );
        }
    }

}

