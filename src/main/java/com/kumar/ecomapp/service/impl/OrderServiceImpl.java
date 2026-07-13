package com.kumar.ecomapp.service.impl;

import com.kumar.ecomapp.dto.OrderItemResponseDTO;
import com.kumar.ecomapp.dto.OrderRequestDTO;
import com.kumar.ecomapp.dto.OrderResponseDTO;
import com.kumar.ecomapp.dto.UpdateOrderDTO;
import com.kumar.ecomapp.entity.Address;
import com.kumar.ecomapp.entity.Cart;
import com.kumar.ecomapp.entity.Order;
import com.kumar.ecomapp.entity.OrderItem;
import com.kumar.ecomapp.entity.Product;
import com.kumar.ecomapp.entity.User;
import com.kumar.ecomapp.entity.enums.OrderStatus;
import com.kumar.ecomapp.entity.enums.ProductStatus;
import com.kumar.ecomapp.exception.address.AddressNotFoundException;
import com.kumar.ecomapp.exception.cart.NoCartItemsFoundException;
import com.kumar.ecomapp.exception.order.NoOrdersFoundException;
import com.kumar.ecomapp.exception.order.OrderNotFoundException;
import com.kumar.ecomapp.exception.user.UserNotFoundException;
import com.kumar.ecomapp.repo.AddressRepository;
import com.kumar.ecomapp.repo.CartRepository;
import com.kumar.ecomapp.repo.OrderItemRepository;
import com.kumar.ecomapp.repo.OrderRepository;
import com.kumar.ecomapp.repo.ProductRepository;
import com.kumar.ecomapp.repo.UserRepository;
import com.kumar.ecomapp.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private static final Logger logger =
            LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepository;

    private final OrderItemRepository orderItemRepository;

    private final UserRepository userRepository;

    private final AddressRepository addressRepository;

    private final CartRepository cartRepository;

    private final ProductRepository productRepository;

    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public OrderResponseDTO createOrder(
            @Valid OrderRequestDTO requestDTO) {

        logger.info(
                "Creating order for user id: {}",
                requestDTO.getUserId()
        );

        User user = userRepository.findById(
                        requestDTO.getUserId())
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with id: "
                                        + requestDTO.getUserId()));

        Address address = addressRepository.findById(
                        requestDTO.getAddressId())
                .orElseThrow(() ->
                        new AddressNotFoundException(
                                "Address not found with id: "
                                        + requestDTO.getAddressId()));

        List<Cart> cartItems =
                cartRepository.findByUserUserId(
                        user.getUserId());

        if (cartItems.isEmpty()) {
            throw new NoCartItemsFoundException(
                    "Cart is empty"
            );
        }

        Order order = new Order();

        order.setUser(user);

        order.setAddress(address);

        order.setStatus(OrderStatus.PENDING);

        BigDecimal totalAmount = BigDecimal.ZERO;

        List<OrderItem> orderItems =
                new ArrayList<>();

        for (Cart cart : cartItems) {

            Product product =
                    cart.getProduct();

            validateProduct(product);

            validateStock(
                    product,
                    cart.getQuantity()
            );

            OrderItem orderItem =
                    new OrderItem();

            orderItem.setOrder(order);

            orderItem.setProduct(product);

            orderItem.setQuantity(
                    cart.getQuantity()
            );

            orderItem.setPrice(
                    product.getPrice()
            );

            BigDecimal subTotal =
                    product.getPrice().multiply(
                            BigDecimal.valueOf(
                                    cart.getQuantity()
                            )
                    );

            orderItem.setSubTotal(subTotal);

            totalAmount =
                    totalAmount.add(subTotal);

            orderItems.add(orderItem);

            product.setStockQuantity(
                    product.getStockQuantity()
                            - cart.getQuantity()
            );

            updateProductStatus(product);

            productRepository.save(product);
        }

        order.setTotalAmount(totalAmount);

        order.setOrderItems(orderItems);

        Order savedOrder =
                orderRepository.save(order);

        cartRepository.deleteByUserUserId(
                user.getUserId()
        );

        logger.info(
                "Order created successfully. orderId={}",
                savedOrder.getOrderId()
        );

        return convertToResponse(savedOrder);
    }

    @Override
    public OrderResponseDTO getOrderById(
            Long orderId) {

        logger.info(
                "Fetching order with id: {}",
                orderId
        );

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new OrderNotFoundException(
                                "Order not found with id: " + orderId
                        ));

        return convertToResponse(order);
    }

    @Override
    public List<OrderResponseDTO> getOrdersByUserId(
            Long userId) {

        userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with id: " + userId
                        ));

        logger.info(
                "Fetching orders for user id: {}",
                userId
        );

        List<Order> orders =
                orderRepository.findByUserUserId(userId);

        if (orders.isEmpty()) {
            throw new NoOrdersFoundException(
                    "No orders found for user id: " + userId
            );
        }

        return orders.stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Override
    public List<OrderResponseDTO> getOrdersByStatus(
            OrderStatus status) {

        logger.info(
                "Fetching orders with status: {}",
                status
        );

        List<Order> orders =
                orderRepository.findByStatus(status);

        if (orders.isEmpty()) {
            throw new NoOrdersFoundException(
                    "No orders found with status: " + status
            );
        }

        return orders.stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Override
    @Transactional
    public OrderResponseDTO updateOrderStatus(
            Long orderId,
            @Valid UpdateOrderDTO updateDTO) {

        logger.info(
                "Updating order status. orderId={}",
                orderId
        );

        if (updateDTO.isEmpty()) {
            throw new IllegalArgumentException(
                    "Order status is required"
            );
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new OrderNotFoundException(
                                "Order not found with id: " + orderId
                        ));

        order.setStatus(updateDTO.getStatus());

        Order updatedOrder =
                orderRepository.save(order);

        logger.info(
                "Order status updated successfully. orderId={}",
                updatedOrder.getOrderId()
        );

        return convertToResponse(updatedOrder);
    }

    @Override
    @Transactional
    public void cancelOrder(
            Long orderId) {

        logger.info(
                "Cancelling order. orderId={}",
                orderId
        );

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new OrderNotFoundException(
                                "Order not found with id: " + orderId
                        ));

        order.setStatus(OrderStatus.CANCELLED);

        orderRepository.save(order);

        logger.info(
                "Order cancelled successfully. orderId={}",
                orderId
        );
    }

    private OrderResponseDTO convertToResponse(
            Order order) {

        OrderResponseDTO dto =
                modelMapper.map(
                        order,
                        OrderResponseDTO.class);

        dto.setUserId(
                order.getUser().getUserId());

        dto.setAddressId(
                order.getAddress().getAddressId());

        dto.setOrderItems(
                order.getOrderItems()
                        .stream()
                        .map(this::convertToOrderItemResponse)
                        .toList());

        return dto;
    }

    private OrderItemResponseDTO convertToOrderItemResponse(
            OrderItem orderItem) {

        OrderItemResponseDTO dto =
                modelMapper.map(
                        orderItem,
                        OrderItemResponseDTO.class);

        dto.setProductId(
                orderItem.getProduct().getProductId());

        dto.setProductName(
                orderItem.getProduct().getProductName());

        dto.setBrand(
                orderItem.getProduct().getBrand());

        return dto;
    }

    private void validateProduct(
            Product product) {

        if (product.getStatus() != ProductStatus.ACTIVE) {
            throw new IllegalArgumentException(
                    "Product is not available"
            );
        }
    }

    private void validateStock(
            Product product,
            Integer quantity) {

        if (quantity > product.getStockQuantity()) {
            throw new IllegalArgumentException(
                    "Insufficient stock for product: "
                            + product.getProductName()
            );
        }
    }

    private void updateProductStatus(
            Product product) {

        if (product.getStockQuantity() <= 0) {

            product.setStatus(
                    ProductStatus.OUT_OF_STOCK
            );

        } else {

            product.setStatus(
                    ProductStatus.ACTIVE
            );
        }
    }

}