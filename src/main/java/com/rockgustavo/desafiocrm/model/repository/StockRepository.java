package com.rockgustavo.desafiocrm.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.rockgustavo.desafiocrm.model.entity.Stock;

public interface StockRepository extends JpaRepository<Stock, Long> {
    Optional<Stock> findByItem_Id(Long itemId);
}
