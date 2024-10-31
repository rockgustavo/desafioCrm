package com.rockgustavo.desafiocrm.service;

import java.util.Optional;

import com.rockgustavo.desafiocrm.model.entity.Customer;
import com.rockgustavo.desafiocrm.rest.dto.CustomerDTO;

public interface CustomerService {

    Optional<CustomerDTO> findByEmailAndPassword(String email, String password);

    CustomerDTO findById(Long id);

    CustomerDTO createCustomer(Customer customer);

    CustomerDTO updateCustomer(Customer customer);
}
