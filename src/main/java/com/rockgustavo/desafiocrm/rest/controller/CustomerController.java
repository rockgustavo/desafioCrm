package com.rockgustavo.desafiocrm.rest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rockgustavo.desafiocrm.exception.CustomerNotFoundException;
import com.rockgustavo.desafiocrm.model.entity.Customer;
import com.rockgustavo.desafiocrm.rest.dto.CustomerDTO;
import com.rockgustavo.desafiocrm.service.CustomerService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody Customer customer) {
        return ResponseEntity.ok(customerService.createCustomer(customer));
    }

    @PutMapping
    public ResponseEntity<CustomerDTO> updateCustomer(@RequestBody Customer customer) {
        customerService.findById(customer.getId());
        return ResponseEntity.ok(customerService.updateCustomer(customer));

    }

    @GetMapping("/login")
    public ResponseEntity<CustomerDTO> login(@RequestParam String email, @RequestParam String password) {
        return customerService.findByEmailAndPassword(email, password)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new CustomerNotFoundException("Usuário ou senha inválidos."));
    }
}
