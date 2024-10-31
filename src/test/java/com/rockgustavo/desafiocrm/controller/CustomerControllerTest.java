package com.rockgustavo.desafiocrm.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

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
import com.rockgustavo.desafiocrm.model.entity.Customer;
import com.rockgustavo.desafiocrm.rest.controller.CustomerController;
import com.rockgustavo.desafiocrm.rest.dto.CustomerDTO;
import com.rockgustavo.desafiocrm.service.CustomerService;

@ActiveProfiles("test")
@WebMvcTest(controllers = CustomerController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CustomerControllerTest {

    private static final String API = "/api/customer";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @Test
    public void deveCriarCustomerComSucesso() throws Exception {
        // Cenário
        Customer customer = Customer.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .password("password123")
                .build();

        CustomerDTO customerDTO = CustomerDTO.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .build();

        BDDMockito.given(customerService.createCustomer(Mockito.any(Customer.class)))
                .willReturn(customerDTO);

        String JSON = new ObjectMapper().writeValueAsString(customer);

        // Execução
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(JSON);

        // Verificação
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    public void deveAtualizarCustomerComSucesso() throws Exception {
        // Cenário
        CustomerDTO customerDTO = CustomerDTO.builder()
                .id(1L)
                .name("John Doe Updated")
                .email("john.doe@example.com")
                .build();

        BDDMockito.given(customerService.updateCustomer(Mockito.any(Customer.class)))
                .willReturn(customerDTO);

        String JSON = new ObjectMapper().writeValueAsString(customerDTO);

        // Execução
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(JSON);

        // Verificação
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe Updated"));
    }

    @Test
    public void deveFazerLoginComSucesso() throws Exception {
        // Cenário
        String email = "john.doe@example.com";
        String password = "password123";
        CustomerDTO customerDTO = CustomerDTO.builder()
                .name("John Doe")
                .email(email)
                .build();

        BDDMockito.given(customerService.findByEmailAndPassword(email, password))
                .willReturn(Optional.of(customerDTO));

        // Execução
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(API + "/login")
                .param("email", email)
                .param("password", password)
                .accept(MediaType.APPLICATION_JSON);

        // Verificação
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    public void deveRetornarErroAoLoginComDadosInvalidos() throws Exception {
        // Cenário
        String email = "invalid@example.com";
        String password = "wrongpassword";

        BDDMockito.given(customerService.findByEmailAndPassword(email, password))
                .willReturn(Optional.empty());

        // Execução
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(API + "/login")
                .param("email", email)
                .param("password", password)
                .accept(MediaType.APPLICATION_JSON);

        // Verificação
        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }
}
