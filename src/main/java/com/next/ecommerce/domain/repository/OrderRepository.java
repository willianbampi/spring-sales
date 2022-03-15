package com.next.ecommerce.domain.repository;

import java.util.Optional;
import java.util.Set;

import com.next.ecommerce.domain.entity.Customer;
import com.next.ecommerce.domain.entity.Order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    Set<Order> findByCustomer(Customer customer);

    @Query("select o from Order o left join fetch p.items where p.id = :id")
    Optional<Order> findByIdFetchItems(@Param("id") Integer id);
    
}
