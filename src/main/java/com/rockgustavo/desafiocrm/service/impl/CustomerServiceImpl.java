package com.rockgustavo.desafiocrm.service.impl;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.rockgustavo.desafiocrm.exception.CustomerNotFoundException;
import com.rockgustavo.desafiocrm.model.entity.Customer;
import com.rockgustavo.desafiocrm.model.repository.CustomerRepository;
import com.rockgustavo.desafiocrm.rest.dto.CustomerDTO;
import com.rockgustavo.desafiocrm.service.CustomerService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;

    public Optional<CustomerDTO> findByEmailAndPassword(String email, String password) {
        Optional<Customer> customerOpt = customerRepository.findByEmail(email);
        if (customerOpt.isPresent()) { // && passwordEncoder.matches(password, customerOpt.get().getPassword())) {
            return Optional.of(modelMapper.map(customerOpt.get(), CustomerDTO.class));
        }
        return Optional.empty();
    }

    public CustomerDTO findById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Cliente com ID " + id + " não encontrado."));
        return modelMapper.map(customer, CustomerDTO.class);
    }

    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        Customer customer = Customer.builder()
                .name(customerDTO.getName())
                .email(customerDTO.getEmail())
                .password(validatePassword(customerDTO.getPassword()))
                .build();
        return modelMapper.map(customerRepository.save(customer), CustomerDTO.class);
    }

    @Transactional
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        Customer customer = Customer.builder()
                .name(customerDTO.getName())
                .email(customerDTO.getEmail())
                .password(validatePassword(customerDTO.getPassword()))
                .build();
        return modelMapper.map(customerRepository.save(customer), CustomerDTO.class);
    }

    private String validatePassword(String password) {
        return password;
        // return (!StringUtils.isEmpty(password)) ? passwordEncoder.encode(password) :
        // password;
    }

}
