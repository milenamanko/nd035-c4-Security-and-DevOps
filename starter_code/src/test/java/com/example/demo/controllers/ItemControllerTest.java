package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class ItemControllerTest {

    private ItemController itemController;

    @Mock
    private ItemRepository itemRepo;

    @BeforeEach
    public void setup() {

        MockitoAnnotations.initMocks(this);

        itemController = new ItemController();

        TestUtils.injectObjects(itemController, "itemRepository", itemRepo);
    }

    @Test
    public void getItemsTest() throws Exception {

        Mockito.when(itemRepo.findAll()).thenReturn(createItems());

        final ResponseEntity<List<Item>> response = itemController.getItems();

        Assertions.assertNotNull(response);
        Assertions.assertEquals(200, response.getStatusCodeValue());

        List<Item> items = response.getBody();

        assertNotNull(items);
        assertEquals(3, items.size());
    }

    @Test
    public void getItemByIdTest() throws Exception {

        Mockito.when(itemRepo.findById(1L)).thenReturn(Optional.of(createItems().get(0)));

        final ResponseEntity<Item> response = itemController.getItemById(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Item item = response.getBody();

        assertNotNull(item);
        assertEquals("jacket", item.getName());
        assertEquals("blue zipped jacket", item.getDescription());
    }

    @Test
    public void getItemByInvalidIdTest() throws Exception {

        final ResponseEntity<Item> response = itemController.getItemById(5L);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void getItemByNameTest() throws Exception {

        Mockito.when(itemRepo.findByName("user")).thenReturn(createItems());

        final ResponseEntity<List<Item>> response = itemController.getItemsByName("user");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<Item> items = response.getBody();

        assertNotNull(items);
        assertEquals("jacket", items.get(0).getName());
        assertEquals("black elegant hat", items.get(1).getDescription());
    }

    @Test
    public void getItemByInvalidNameTest() throws Exception {

        final ResponseEntity<List<Item>> response = itemController.getItemsByName("userNotFound");

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    public List<Item> createItems() {
        return (Arrays.asList(new Item(1L, "jacket", BigDecimal.valueOf(109.99), "blue zipped jacket"),
                new Item(2L, "hat", BigDecimal.valueOf(50.00), "black elegant hat"),
                new Item(3L, "trousers", BigDecimal.valueOf(89.90), "black cotton trousers")));
    }
}
