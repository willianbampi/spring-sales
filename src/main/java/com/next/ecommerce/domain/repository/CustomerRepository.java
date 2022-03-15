package com.next.ecommerce.domain.repository;

import java.util.List;

import com.next.ecommerce.domain.entity.Customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    List<Customer> findByNameLike(String name);

    @Query("select c from Customer c left join fetch c.orders where c.id =:id")
    Customer findCustomerFetchOrders(@Param("id") Integer id);
    
}
