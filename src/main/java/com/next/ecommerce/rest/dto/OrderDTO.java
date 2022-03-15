package com.next.ecommerce.rest.dto;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.next.ecommerce.validation.NotEmptyList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    @NotNull(message = "{required.customer.id}")
    private Integer customer;

    @NotNull(message = "{required.order.amount}")
    private BigDecimal amount;

    @NotEmptyList(message = "{required.order.items}")
    private List<OrderItemDTO> items;
    
}
