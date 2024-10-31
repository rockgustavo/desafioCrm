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
import com.rockgustavo.desafiocrm.model.repository.ItemRepository;

@ActiveProfiles("test")
@DataJpaTest
public class ItemRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ItemRepository itemRepository;

    private Item item;

    @BeforeEach
    public void setUp() {
        item = Item.builder()
                .name("Item Teste")
                .price(100.0)
                .build();
        entityManager.persist(item);
    }

    @Test
    @DisplayName("Deve salvar um item")
    public void saveItemTest() {
        Item savedItem = itemRepository.save(item);

        assertThat(savedItem.getId()).isNotNull();
        assertThat(savedItem.getName()).isEqualTo("Item Teste");
    }

    @Test
    @DisplayName("Deve buscar um item por ID")
    public void findByIdTest() {
        Optional<Item> foundItem = itemRepository.findById(item.getId());

        assertThat(foundItem.isPresent()).isTrue();
        assertThat(foundItem.get().getId()).isEqualTo(item.getId());
    }

    @Test
    @DisplayName("Deve deletar um item")
    public void deleteItemTest() {
        itemRepository.delete(item);
        Item deletedItem = entityManager.find(Item.class, item.getId());
        assertThat(deletedItem).isNull();
    }
}
