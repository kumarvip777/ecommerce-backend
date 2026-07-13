package com.kumar.ecomapp.repo;

import com.kumar.ecomapp.entity.Order;
import com.kumar.ecomapp.entity.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserUserId(
            Long userId
    );

    List<Order> findByStatus(
            OrderStatus status
    );

    List<Order> findByUserUserIdAndStatus(
            Long userId,
            OrderStatus status
    );

}