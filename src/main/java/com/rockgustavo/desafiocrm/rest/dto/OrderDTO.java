package com.rockgustavo.desafiocrm.rest.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.rockgustavo.desafiocrm.model.entity.Customer;

import jakarta.validation.constraints.NotBlank;
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
    private LocalDateTime orderDate;
    @NotBlank(message = "Este pedido precisa pertencer à um cliente!")
    private Customer customer;
    private List<OrderItemDTO> orderItems;
}
