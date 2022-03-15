package com.next.ecommerce.domain.repository;

import com.next.ecommerce.domain.entity.Item;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    
}
