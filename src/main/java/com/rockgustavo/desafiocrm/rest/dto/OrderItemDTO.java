package com.rockgustavo.desafiocrm.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO {
    private Long id;
    private Long itemId;
    private Integer quantity;
}
