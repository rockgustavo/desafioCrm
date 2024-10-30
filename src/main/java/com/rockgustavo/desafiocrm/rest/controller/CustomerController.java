package com.rockgustavo.desafiocrm.rest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rockgustavo.desafiocrm.rest.dto.CustomerDTO;
import com.rockgustavo.desafiocrm.service.CustomerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@Valid @RequestBody CustomerDTO customerDTO) {
        return ResponseEntity.ok(customerService.createCustomer(customerDTO));
    }

    @PutMapping
    public ResponseEntity<CustomerDTO> updateCustomer(@Valid @RequestBody CustomerDTO customerDTO) {
        return ResponseEntity.ok(customerService.updateCustomer(customerDTO));
    }

    @GetMapping("/login")
    public ResponseEntity<CustomerDTO> login(@Valid @RequestParam String email, @RequestParam String password) {
        return ResponseEntity.ok(customerService.findByEmailAndPassword(email, password).get());
    }
}
