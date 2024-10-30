package com.rockgustavo.desafiocrm.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rockgustavo.desafiocrm.model.entity.Item;

public interface ProductRepository extends JpaRepository<Item, Long> {

}
