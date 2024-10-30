package com.rockgustavo.desafiocrm.rest.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private Long id;
    private LocalDate orderDate;
    private List<OrderItemDTO> orderItems; // Usando um DTO específico para OrderItem
}
