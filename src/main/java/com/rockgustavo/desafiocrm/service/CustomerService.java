package com.rockgustavo.desafiocrm.service;

import java.util.Optional;

import com.rockgustavo.desafiocrm.rest.dto.CustomerDTO;

public interface CustomerService {

    Optional<CustomerDTO> findByEmailAndPassword(String email, String password);

    CustomerDTO findById(Long id);

    CustomerDTO createCustomer(CustomerDTO customerDTO);

    CustomerDTO updateCustomer(CustomerDTO customerDTO);
}
