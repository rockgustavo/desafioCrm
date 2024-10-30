package com.rockgustavo.desafiocrm.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rockgustavo.desafiocrm.model.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
