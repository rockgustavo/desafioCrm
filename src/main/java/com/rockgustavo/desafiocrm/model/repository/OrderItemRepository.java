package com.rockgustavo.desafiocrm.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rockgustavo.desafiocrm.model.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
