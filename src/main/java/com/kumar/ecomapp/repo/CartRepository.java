package com.kumar.ecomapp.repo;

import com.kumar.ecomapp.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    boolean existsByUserUserIdAndProductProductId(
            Long userId,
            Long productId
    );

    Optional<Cart> findByUserUserIdAndProductProductId(
            Long userId,
            Long productId
    );

    List<Cart> findByUserUserId(
            Long userId
    );

    List<Cart> findByProductProductId(
            Long productId
    );

    void deleteByUserUserId(
            Long userId
    );

}