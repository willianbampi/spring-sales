package com.next.ecommerce.rest.controller;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.next.ecommerce.domain.entity.Order;
import com.next.ecommerce.domain.entity.OrderItem;
import com.next.ecommerce.domain.enums.OrderStatus;
import com.next.ecommerce.rest.dto.OrderDTO;
import com.next.ecommerce.rest.dto.OrderStatusDTO;
import com.next.ecommerce.rest.dto.ResponseOrderDTO;
import com.next.ecommerce.rest.dto.ResponseOrderItemDTO;
import com.next.ecommerce.service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/order")
@Api("Order API")
public class OrderController {

    private static String NOT_FOUND_MESSAGE = "Not Found";

    @Autowired
    private OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)@ApiOperation("Save a new order")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description =  "Success."),
        @ApiResponse(responseCode = "400", description = "Validation error(s).")
    })
    public Integer save(@RequestBody @Valid @ApiParam("Order") OrderDTO orderDTO) {
        Order order = orderService.save(orderDTO);
        return order.getId();
    }

    @GetMapping("{id}")
    @ApiOperation("Get order details")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description =  "Success."),
        @ApiResponse(responseCode = "404", description = "Order not found.")
    })
    public ResponseOrderDTO getOrderById(@PathVariable @ApiParam("Order id") Integer id) {
        return orderService.getFullOrder(id)
                .map(order -> orderParser(order))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, NOT_FOUND_MESSAGE));
    }

    private ResponseOrderDTO orderParser(Order order) {
        return ResponseOrderDTO.builder()
                .id(order.getId())
                .customerCPF(order.getCustomer().getCpf())
                .customerName(order.getCustomer().getName())
                .amount(order.getAmount())
                .orderDate(order.getOrderDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .status(order.getStatus().name())
                .items(itemsParser(order.getItems()))
                .build();
    }

    private List<ResponseOrderItemDTO> itemsParser(List<OrderItem> items) {
        if(CollectionUtils.isEmpty(items)) {
            return Collections.EMPTY_LIST;
        }

        return items.stream()
                .map(
                    item -> ResponseOrderItemDTO
                                .builder()
                                .description(item.getItem().getDescription())
                                .price(item.getItem().getPrice())
                                .quantity(item.getQuantity())
                                .build()
                ).collect(Collectors.toList());
    }

    @PatchMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Update order status")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description =  "Success."),
        @ApiResponse(responseCode = "404", description = "Order not found.")
    })
    public void updateOrderStatus(@RequestBody @ApiParam("Order status") OrderStatusDTO orderStatusDTO, @PathVariable @ApiParam("Order id") Integer id) {
        String newOrderStatus = orderStatusDTO.getNewOrderStatus();
        orderService.updateOrderStatus(id, OrderStatus.valueOf(newOrderStatus));
    }
    
}
