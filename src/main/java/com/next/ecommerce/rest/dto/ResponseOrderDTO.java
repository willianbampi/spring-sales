package com.next.ecommerce.rest.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseOrderDTO {

    private Integer id;

    private String customerCPF;

    private String customerName;

    private BigDecimal amount;

    private String orderDate;

    private List<ResponseOrderItemDTO> items;

    private String status;
    
}
