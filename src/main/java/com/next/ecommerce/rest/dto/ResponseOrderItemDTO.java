package com.next.ecommerce.rest.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseOrderItemDTO {

    private String description;

    private BigDecimal price;

    private Integer quantity;
    
}
