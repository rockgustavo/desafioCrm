package com.rockgustavo.desafiocrm.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rockgustavo.desafiocrm.rest.controller.OrderController;
import com.rockgustavo.desafiocrm.rest.dto.CustomerDTO;
import com.rockgustavo.desafiocrm.rest.dto.OrderDTO;
import com.rockgustavo.desafiocrm.rest.dto.OrderItemDTO;
import com.rockgustavo.desafiocrm.service.OrderService;

@ActiveProfiles("test")
@WebMvcTest(controllers = OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
public class OrderControllerTest {
    private static final String API = "/api/order";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Test
    public void deveCriarOrderComSucesso() throws Exception {
        // Cenário
        OrderDTO orderDTO = OrderDTO.builder()
                .customer(CustomerDTO.builder().id(1L).build())
                .orderItems(Arrays.asList(new OrderItemDTO(1L, 1L, 2)))
                .build();

        BDDMockito.given(orderService.createOrder(Mockito.any(OrderDTO.class)))
                .willReturn(orderDTO);

        String JSON = new ObjectMapper().writeValueAsString(orderDTO);

        // Execução
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(JSON);

        // Verificação
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customer.id").value(1));
    }

    @Test
    public void deveBuscarOrderComSucesso() throws Exception {
        // Cenário
        Long orderId = 1L;
        OrderDTO orderDTO = OrderDTO.builder()
                .customer(CustomerDTO.builder().id(1L).build())
                .orderItems(Arrays.asList(new OrderItemDTO(1L, 1L, 2)))
                .build();

        BDDMockito.given(orderService.getOrder(orderId))
                .willReturn(orderDTO);

        // Execução
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(API)
                .param("numeroPedido", orderId.toString())
                .accept(MediaType.APPLICATION_JSON);

        // Verificação
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customer.id").value(1));
    }

    @Test
    public void deveExcluirOrderComSucesso() throws Exception {
        // Cenário
        Long orderId = 1L;
        BDDMockito.doNothing().when(orderService).deleteOrder(Mockito.eq(orderId));

        // Execução
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(API + "/{id}", orderId)
                .accept(MediaType.APPLICATION_JSON);

        // Verificação
        mockMvc.perform(request)
                .andExpect(status().isNoContent());
    }
}
