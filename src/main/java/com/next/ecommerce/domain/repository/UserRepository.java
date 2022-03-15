package com.next.ecommerce.domain.repository;

import java.util.Optional;

import com.next.ecommerce.domain.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);
    
}
