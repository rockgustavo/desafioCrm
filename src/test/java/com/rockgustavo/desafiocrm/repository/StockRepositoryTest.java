package com.rockgustavo.desafiocrm.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import com.rockgustavo.desafiocrm.model.entity.Item;
import com.rockgustavo.desafiocrm.model.entity.Stock;
import com.rockgustavo.desafiocrm.model.repository.StockRepository;

@ActiveProfiles("test")
@DataJpaTest
public class StockRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private StockRepository stockRepository;

    private Stock stock;
    private Item item;

    @BeforeEach
    public void setUp() {
        item = Item.builder()
                .name("Item Teste")
                .price(100.0)
                .build();
        entityManager.persist(item);

        stock = Stock.builder()
                .quantity(50)
                .item(item)
                .build();
        entityManager.persist(stock);
    }

    @Test
    @DisplayName("Deve encontrar estoque por ID do item")
    public void findByItemIdTest() {
        Optional<Stock> foundStock = stockRepository.findByItem_Id(item.getId());

        assertThat(foundStock.isPresent()).isTrue();
        assertThat(foundStock.get().getItem().getId()).isEqualTo(item.getId());
    }

    @Test
    @DisplayName("Deve salvar um estoque")
    public void saveStockTest() {
        Stock savedStock = stockRepository.save(stock);

        assertThat(savedStock.getId()).isNotNull();
        assertThat(savedStock.getQuantity()).isEqualTo(50);
    }

    @Test
    @DisplayName("Deve deletar um estoque")
    public void deleteStockTest() {
        stockRepository.delete(stock);
        Stock deletedStock = entityManager.find(Stock.class, stock.getId());
        assertThat(deletedStock).isNull();
    }
}
