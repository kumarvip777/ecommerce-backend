package com.kumar.ecomapp.repo;

import com.kumar.ecomapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    boolean existsByMobileNumber(String phoneNumber);

    List<User> findByUserNameContainingIgnoreCase(String firstName);
}
