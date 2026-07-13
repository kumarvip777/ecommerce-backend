package com.kumar.ecomapp.repo;

import com.kumar.ecomapp.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrderOrderId(
            Long orderId
    );

    List<OrderItem> findByProductProductId(
            Long productId
    );

}