package com.rockgustavo.desafiocrm.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.rockgustavo.desafiocrm.exception.InsufficientStockException;
import com.rockgustavo.desafiocrm.model.entity.Order;
import com.rockgustavo.desafiocrm.model.entity.OrderItem;
import com.rockgustavo.desafiocrm.model.entity.Stock;
import com.rockgustavo.desafiocrm.model.repository.OrderRepository;
import com.rockgustavo.desafiocrm.model.repository.StockRepository;
import com.rockgustavo.desafiocrm.rest.dto.OrderDTO;
import com.rockgustavo.desafiocrm.rest.dto.OrderItemDTO;
import com.rockgustavo.desafiocrm.service.OrderService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final StockRepository stockRepository;
    private final ModelMapper modelMapper;

    public OrderDTO createOrder(OrderDTO orderDTO) {
        Order order = Order.builder()
                .orderDate(LocalDateTime.now())
                .customer(orderDTO.getCustomer())
                .orderItems(new ArrayList<>())
                .build();

        for (OrderItemDTO orderItemDTO : orderDTO.getOrderItems()) {
            // Verifica se o item existe no estoque
            Stock stock = stockRepository.findByItem_Id(orderItemDTO.getItemId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Item com ID " + orderItemDTO.getItemId() + " não encontrado no estoque."));

            // Verifica se há estoque suficiente
            if (stock.getQuantity() < orderItemDTO.getQuantity()) {
                throw new InsufficientStockException(
                        "Quantidade não disponível para o item: " + stock.getItem().getName());
            }

            // Adiciona o item ao pedido
            OrderItem orderItem = OrderItem.builder()
                    .item(stock.getItem())
                    .order(order)
                    .quantity(orderItemDTO.getQuantity())
                    .build();
            order.getOrderItems().add(orderItem);

            // Atualiza o estoque
            stock.setQuantity(stock.getQuantity() - orderItemDTO.getQuantity());
            stockRepository.save(stock);
        }

        return modelMapper.map(orderRepository.save(order), OrderDTO.class);
    }

    @SuppressWarnings("deprecation")
    public OrderDTO getOrder(Long numeroPedido) {
        return modelMapper.map(orderRepository.getById(numeroPedido), OrderDTO.class);
    }

    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }

}
