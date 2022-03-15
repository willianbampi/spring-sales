package com.next.ecommerce.domain.repository;

import com.next.ecommerce.domain.entity.OrderItem;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    
}
