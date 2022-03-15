package com.next.ecommerce.service.implementation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.next.ecommerce.domain.entity.Customer;
import com.next.ecommerce.domain.entity.Item;
import com.next.ecommerce.domain.entity.Order;
import com.next.ecommerce.domain.entity.OrderItem;
import com.next.ecommerce.domain.enums.OrderStatus;
import com.next.ecommerce.domain.repository.CustomerRepository;
import com.next.ecommerce.domain.repository.ItemRepository;
import com.next.ecommerce.domain.repository.OrderItemRepository;
import com.next.ecommerce.domain.repository.OrderRepository;
import com.next.ecommerce.exception.BusinessRuleException;
import com.next.ecommerce.exception.NotFoundException;
import com.next.ecommerce.rest.dto.OrderDTO;
import com.next.ecommerce.rest.dto.OrderItemDTO;
import com.next.ecommerce.service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImplementation implements OrderService {

    @Autowired
    private final OrderRepository orderRepository;

    @Autowired
    private final CustomerRepository customerRepository;

    @Autowired
    private final ItemRepository itemRepository;

    @Autowired
    private final OrderItemRepository orderItemRepository;

    @Override
    @Transactional
    public Order save(OrderDTO orderDTO) {
        Integer customerId = orderDTO.getCustomer();
        Customer customer = customerRepository
                                .findById(customerId)
                                .orElseThrow(() -> new BusinessRuleException("Invalid Customer Id " + customerId));
        
        Order order = new Order();
        order.setStatus(OrderStatus.PENDING);
        order.setAmount(orderDTO.getAmount());
        order.setOrderDate(LocalDate.now());
        order.setCustomer(customer);
        
        List<OrderItem> orderItems = parseItems(order, orderDTO.getItems());
        orderRepository.save(order);
        orderItemRepository.saveAll(orderItems);
        order.setItems(orderItems);
        order.setStatus(OrderStatus.COMPLETED);
        orderRepository.save(order);
        return order;
    }

    private List<OrderItem> parseItems(Order order, List<OrderItemDTO> items) {
        if(CollectionUtils.isEmpty(items)) {
            throw new BusinessRuleException("It is required at least one item to save order");
        }

        return items.stream()
                .map(orderItemDTO -> {
                    Integer itemId = orderItemDTO.getItem();
                    Item item = itemRepository
                                        .findById(itemId)
                                        .orElseThrow(() -> new BusinessRuleException("Invalid Item Id " + itemId));
                    OrderItem orderItem = new OrderItem();
                    orderItem.setQuantity(orderItemDTO.getQuantity());
                    orderItem.setOrder(order);
                    orderItem.setItem(item);
                    return orderItem;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Order> getFullOrder(Integer id) {
        return orderRepository.findByIdFetchItems(id);
    }

    @Override
    @Transactional
    public void updateOrderStatus(Integer id, OrderStatus orderStatus) {
        orderRepository.findById(id)
            .map(order -> {
                order.setStatus(orderStatus);
                return orderRepository.save(order);
            })
            .orElseThrow(() -> new NotFoundException("Order not found for id " + id));
    }

}
