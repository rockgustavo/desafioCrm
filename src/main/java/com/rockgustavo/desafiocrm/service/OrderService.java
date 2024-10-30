package com.rockgustavo.desafiocrm.service;

import com.rockgustavo.desafiocrm.rest.dto.OrderDTO;

public interface OrderService {
    OrderDTO createOrder(OrderDTO orderDTO);

    OrderDTO getOrder(Long numeroPedido);

    void deleteOrder(Long orderId);
}
