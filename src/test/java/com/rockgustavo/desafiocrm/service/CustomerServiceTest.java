package com.rockgustavo.desafiocrm.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.rockgustavo.desafiocrm.exception.CustomerNotFoundException;
import com.rockgustavo.desafiocrm.model.entity.Customer;
import com.rockgustavo.desafiocrm.model.repository.CustomerRepository;
import com.rockgustavo.desafiocrm.rest.dto.CustomerDTO;
import com.rockgustavo.desafiocrm.service.impl.CustomerServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CustomerServiceImpl service;

    private Customer customer;
    private CustomerDTO customerDTO;

    @BeforeEach
    public void setUp() {
        customer = Customer.builder()
                .id(1L)
                .name("Cliente Teste")
                .email("cliente@teste.com")
                .password("encodedPassword") // Senha codificada
                .build();

        customerDTO = CustomerDTO.builder()
                .id(1L)
                .name("Cliente Teste")
                .email("cliente@teste.com")
                .build();
    }

    @Test
    @DisplayName("Deve autenticar cliente com email e senha válidos")
    public void findByEmailAndPasswordTest() {
        // Cenário
        String rawPassword = "rawPassword";
        given(customerRepository.findByEmail(customer.getEmail())).willReturn(Optional.of(customer));
        given(passwordEncoder.matches(rawPassword, customer.getPassword())).willReturn(true);
        given(modelMapper.map(customer, CustomerDTO.class)).willReturn(customerDTO);

        // Execução
        Optional<CustomerDTO> result = service.findByEmailAndPassword(customer.getEmail(), rawPassword);

        // Verificação
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(customerDTO.getId());

        verify(customerRepository, times(1)).findByEmail(customer.getEmail());
        verify(passwordEncoder, times(1)).matches(rawPassword, customer.getPassword());
        verify(modelMapper, times(1)).map(customer, CustomerDTO.class);
    }

    @Test
    @DisplayName("Deve retornar vazio para email ou senha inválidos")
    public void findByEmailAndPasswordInvalidTest() {
        // Cenário
        String rawPassword = "wrongPassword";
        given(customerRepository.findByEmail(customer.getEmail())).willReturn(Optional.of(customer));
        given(passwordEncoder.matches(rawPassword, customer.getPassword())).willReturn(false);

        // Execução
        Optional<CustomerDTO> result = service.findByEmailAndPassword(customer.getEmail(), rawPassword);

        // Verificação
        assertThat(result).isEmpty();
        verify(customerRepository, times(1)).findByEmail(customer.getEmail());
        verify(passwordEncoder, times(1)).matches(rawPassword, customer.getPassword());
    }

    @Test
    @DisplayName("Deve buscar cliente por ID com sucesso")
    public void findByIdTest() {
        // Cenário
        given(customerRepository.findById(customer.getId())).willReturn(Optional.of(customer));
        given(modelMapper.map(customer, CustomerDTO.class)).willReturn(customerDTO);

        // Execução
        CustomerDTO result = service.findById(customer.getId());

        // Verificação
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(customerDTO.getId());

        verify(customerRepository, times(1)).findById(customer.getId());
        verify(modelMapper, times(1)).map(customer, CustomerDTO.class);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar cliente inexistente")
    public void findByIdNotFoundTest() {
        // Cenário
        Long invalidId = 999L;
        given(customerRepository.findById(invalidId)).willReturn(Optional.empty());

        // Execução e Verificação
        assertThatThrownBy(() -> service.findById(invalidId))
                .isInstanceOf(CustomerNotFoundException.class)
                .hasMessageContaining("Cliente com ID " + invalidId + " não encontrado.");

        verify(customerRepository, times(1)).findById(invalidId);
    }

    @Test
    @DisplayName("Deve criar um cliente com sucesso")
    public void createCustomerTest() {
        // Cenário
        Customer newCustomer = Customer.builder()
                .name("Cliente Novo")
                .email("novocliente@teste.com")
                .password("newRawPassword")
                .build();

        Customer savedCustomer = Customer.builder()
                .id(2L)
                .name("Cliente Novo")
                .email("novocliente@teste.com")
                .password("encodedPassword")
                .build();

        CustomerDTO savedCustomerDTO = CustomerDTO.builder()
                .id(2L)
                .name("Cliente Novo")
                .email("novocliente@teste.com")
                .build();

        given(passwordEncoder.encode(newCustomer.getPassword())).willReturn("encodedPassword");
        given(customerRepository.save(any(Customer.class))).willReturn(savedCustomer);
        given(modelMapper.map(savedCustomer, CustomerDTO.class)).willReturn(savedCustomerDTO);

        // Execução
        CustomerDTO result = service.createCustomer(newCustomer);

        // Verificação
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(savedCustomerDTO.getId());

        verify(passwordEncoder, times(1)).encode(newCustomer.getPassword());
        verify(customerRepository, times(1)).save(any(Customer.class));
        verify(modelMapper, times(1)).map(savedCustomer, CustomerDTO.class);
    }

    @Test
    @DisplayName("Deve atualizar um cliente com sucesso")
    public void updateCustomerTest() {
        // Cenário
        Customer updatedCustomer = Customer.builder()
                .id(1L)
                .name("Cliente Atualizado")
                .email("cliente@teste.com")
                .password("newRawPassword")
                .build();

        Customer savedCustomer = Customer.builder()
                .id(1L)
                .name("Cliente Atualizado")
                .email("cliente@teste.com")
                .password("encodedPassword")
                .build();

        CustomerDTO updatedCustomerDTO = CustomerDTO.builder()
                .id(1L)
                .name("Cliente Atualizado")
                .email("cliente@teste.com")
                .build();

        given(passwordEncoder.encode(updatedCustomer.getPassword())).willReturn("encodedPassword");
        given(customerRepository.save(any(Customer.class))).willReturn(savedCustomer);
        given(modelMapper.map(savedCustomer, CustomerDTO.class)).willReturn(updatedCustomerDTO);

        // Execução
        CustomerDTO result = service.updateCustomer(updatedCustomer);

        // Verificação
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(updatedCustomerDTO.getId());

        verify(passwordEncoder, times(1)).encode(updatedCustomer.getPassword());
        verify(customerRepository, times(1)).save(any(Customer.class));
        verify(modelMapper, times(1)).map(savedCustomer, CustomerDTO.class);
    }
}
