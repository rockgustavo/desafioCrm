package com.rockgustavo.desafiocrm.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.rockgustavo.desafiocrm.exception.InsufficientStockException;
import com.rockgustavo.desafiocrm.exception.OrderNotFoundException;
import com.rockgustavo.desafiocrm.model.entity.Customer;
import com.rockgustavo.desafiocrm.model.entity.Item;
import com.rockgustavo.desafiocrm.model.entity.Order;
import com.rockgustavo.desafiocrm.model.entity.Stock;
import com.rockgustavo.desafiocrm.model.repository.OrderRepository;
import com.rockgustavo.desafiocrm.model.repository.StockRepository;
import com.rockgustavo.desafiocrm.rest.dto.CustomerDTO;
import com.rockgustavo.desafiocrm.rest.dto.OrderDTO;
import com.rockgustavo.desafiocrm.rest.dto.OrderItemDTO;
import com.rockgustavo.desafiocrm.service.impl.OrderServiceImpl;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private StockRepository stockRepository;

    @Mock
    private ModelMapper modelMapper;

    private OrderServiceImpl service;

    @BeforeEach
    public void setUp() {
        this.service = new OrderServiceImpl(orderRepository, stockRepository, modelMapper);
    }

    @Test
    @DisplayName("Deve criar um pedido com sucesso")
    public void createOrderTest() {
        // Cenário
        OrderItemDTO orderItemDTO = OrderItemDTO.builder().id(1L).itemId(1L).quantity(5).build();
        List<OrderItemDTO> orderItems = new ArrayList<>();
        orderItems.add(orderItemDTO);

        OrderDTO orderDTO = OrderDTO.builder()
                .customer(CustomerDTO.builder().id(1L).build())
                .orderItems(orderItems)
                .build();

        Stock stock = Stock.builder()
                .quantity(10)
                .item(Item.builder().id(1L).name("item1").build())
                .build();

        Order order = Order.builder()
                .id(1L)
                .customer(Customer.builder().id(1L).build())
                .orderDate(LocalDateTime.now())
                .orderItems(new ArrayList<>())
                .build();

        BDDMockito.given(stockRepository.findByItem_Id(orderItemDTO.getItemId())).willReturn(Optional.of(stock));
        BDDMockito.given(orderRepository.save(any(Order.class))).willReturn(order);
        BDDMockito.given(modelMapper.map(order, OrderDTO.class)).willReturn(orderDTO);

        // Execução
        OrderDTO result = service.createOrder(orderDTO);

        // Verificação
        assertThat(result).isNotNull();
        assertThat(result.getCustomer().getId()).isEqualTo(1L);
        assertThat(result.getOrderItems()).hasSize(1);

        BDDMockito.verify(stockRepository, Mockito.times(1)).findByItem_Id(orderItemDTO.getItemId());
        BDDMockito.verify(orderRepository, Mockito.times(1)).save(any(Order.class));
        BDDMockito.verify(modelMapper, Mockito.times(1)).map(order, OrderDTO.class);
    }

    @Test
    @DisplayName("Deve lançar exceção se o estoque não for suficiente ao criar um pedido")
    public void createOrderInsufficientStockTest() {
        // Cenário
        OrderItemDTO orderItemDTO = OrderItemDTO.builder().id(1L).itemId(1L).quantity(15).build(); // Pedido de 15 itens
        List<OrderItemDTO> orderItems = new ArrayList<>();
        orderItems.add(orderItemDTO);

        OrderDTO orderDTO = OrderDTO.builder()
                .customer(CustomerDTO.builder().id(1L).build())
                .orderItems(orderItems)
                .build();

        Stock stock = Stock.builder()
                .quantity(10)
                .item(Item.builder().id(1L).name("item1").build())
                .build();

        // Simula o comportamento do repositório
        BDDMockito.given(stockRepository.findByItem_Id(orderItemDTO.getItemId())).willReturn(Optional.of(stock));

        // Execução e Verificação
        assertThatThrownBy(() -> service.createOrder(orderDTO))
                .isInstanceOf(InsufficientStockException.class)
                .hasMessageContaining("Quantidade não disponível para o item: item1");

        BDDMockito.verify(stockRepository, Mockito.times(1)).findByItem_Id(orderItemDTO.getItemId());
        BDDMockito.verify(orderRepository, Mockito.never()).save(any(Order.class));
    }

    @Test
    @DisplayName("Deve buscar um pedido por ID com sucesso")
    public void getOrderTest() {
        // Cenário
        Long orderId = 1L;
        Order order = Order.builder()
                .id(orderId)
                .customer(Customer.builder().id(1L).build())
                .orderDate(LocalDateTime.now())
                .orderItems(new ArrayList<>())
                .build();

        OrderDTO orderDTO = OrderDTO.builder()
                .id(orderId)
                .customer(CustomerDTO.builder().id(1L).build())
                .build();

        // Simula o comportamento do repositório e do modelMapper
        BDDMockito.given(orderRepository.findById(orderId)).willReturn(Optional.of(order));
        BDDMockito.given(modelMapper.map(order, OrderDTO.class)).willReturn(orderDTO);

        // Execução
        OrderDTO result = service.getOrder(orderId);

        // Verificação
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(orderId);

        BDDMockito.verify(orderRepository, Mockito.times(1)).findById(orderId);
        BDDMockito.verify(modelMapper, Mockito.times(1)).map(order, OrderDTO.class);
    }

    @Test
    @DisplayName("Deve lançar exceção se o pedido não for encontrado")
    public void getOrderNotFoundTest() {
        // Cenário
        Long orderId = 1L;

        // Simula o comportamento do repositório
        BDDMockito.given(orderRepository.findById(orderId)).willReturn(Optional.empty());

        // Execução e Verificação
        assertThatThrownBy(() -> service.getOrder(orderId))
                .isInstanceOf(OrderNotFoundException.class)
                .hasMessageContaining("Pedido com ID: 1 não encontrado.");

        BDDMockito.verify(orderRepository, Mockito.times(1)).findById(orderId);
    }

    @Test
    @DisplayName("Deve deletar um pedido com sucesso")
    public void deleteOrderTest() {
        // Cenário
        Long orderId = 1L;

        // Execução
        service.deleteOrder(orderId);

        // Verificação
        BDDMockito.verify(orderRepository, Mockito.times(1)).deleteById(orderId);
    }
}
