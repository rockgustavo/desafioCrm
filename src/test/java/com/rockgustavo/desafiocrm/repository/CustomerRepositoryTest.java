package com.rockgustavo.desafiocrm.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import com.rockgustavo.desafiocrm.model.entity.Customer;
import com.rockgustavo.desafiocrm.model.repository.CustomerRepository;

@ActiveProfiles("test")
@DataJpaTest
public class CustomerRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CustomerRepository customerRepository;

    private Customer customer;

    @BeforeEach
    public void setUp() {
        customer = Customer.builder()
                .name("Cliente Teste")
                .email("cliente@teste.com")
                .password("encodedPassword")
                .build();
        entityManager.persist(customer);
    }

    @Test
    @DisplayName("Deve encontrar um cliente pelo email")
    public void findByEmailTest() {
        Optional<Customer> foundCustomer = customerRepository.findByEmail("cliente@teste.com");

        assertThat(foundCustomer.isPresent()).isTrue();
        assertThat(foundCustomer.get().getEmail()).isEqualTo("cliente@teste.com");
    }

    @Test
    @DisplayName("Deve salvar um cliente")
    public void saveCustomerTest() {
        Customer savedCustomer = customerRepository.save(customer);

        assertThat(savedCustomer.getId()).isNotNull();
        assertThat(savedCustomer.getEmail()).isEqualTo("cliente@teste.com");
    }

    @Test
    @DisplayName("Deve deletar um cliente")
    public void deleteCustomerTest() {
        customerRepository.delete(customer);
        Customer deletedCustomer = entityManager.find(Customer.class, customer.getId());
        assertThat(deletedCustomer).isNull();
    }
}
