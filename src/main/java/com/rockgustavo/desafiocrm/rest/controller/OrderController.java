package com.rockgustavo.desafiocrm.rest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rockgustavo.desafiocrm.rest.dto.OrderDTO;
import com.rockgustavo.desafiocrm.service.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody OrderDTO orderDTO) {
        return ResponseEntity.ok(orderService.createOrder(orderDTO));
    }

    @GetMapping
    public ResponseEntity<OrderDTO> getOrder(@Valid @RequestParam Long numeroPedido) {
        return ResponseEntity.ok(orderService.getOrder(numeroPedido));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@Valid @PathVariable Long id) {
        orderService.getOrder(id);
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
