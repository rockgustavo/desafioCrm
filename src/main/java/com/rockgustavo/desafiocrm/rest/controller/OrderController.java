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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "Criar Ordem", description = "Cria uma nova ordem de pedido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ordem criada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDTO.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida")
    })
    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody OrderDTO orderDTO) {
        return ResponseEntity.ok(orderService.createOrder(orderDTO));
    }

    @Operation(summary = "Obter Ordem de Pedido", description = "Obtém uma ordem pelo número do pedido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ordem encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDTO.class))),
            @ApiResponse(responseCode = "404", description = "Ordem não encontrada")
    })
    @GetMapping
    public ResponseEntity<OrderDTO> getOrder(@Valid @RequestParam Long numeroPedido) {
        return ResponseEntity.ok(orderService.getOrder(numeroPedido));
    }

    @Operation(summary = "Deletar Ordem", description = "Deleta uma ordem pelo ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Ordem deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Ordem não encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@Valid @PathVariable Long id) {
        orderService.getOrder(id);
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
