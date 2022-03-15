package com.next.ecommerce.service;

import java.util.Optional;

import com.next.ecommerce.domain.entity.Order;
import com.next.ecommerce.domain.enums.OrderStatus;
import com.next.ecommerce.rest.dto.OrderDTO;

public interface OrderService {

    Order save(OrderDTO orderDTO);

    Optional<Order> getFullOrder(Integer id);

    void updateOrderStatus(Integer id, OrderStatus orderStatus);
    
}
