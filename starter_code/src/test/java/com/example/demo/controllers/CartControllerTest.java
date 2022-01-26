package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
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
public class CartControllerTest {

    CartController cartController;

    @Mock
    private UserRepository userRepo;

    @Mock
    private CartRepository cartRepo;

    @Mock
    private ItemRepository itemRepo;

    @BeforeEach
    public void setup() {

        MockitoAnnotations.initMocks(this);

        cartController = new CartController();

        TestUtils.injectObjects(cartController, "userRepository", userRepo);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepo);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepo);
    }

    @Test
    public void addToCartTest() throws Exception {

        Mockito.when(userRepo.findByUsername("user")).thenReturn(createUser());
        Mockito.when(itemRepo.findById(1L)).thenReturn(Optional.of(createItems().get(0)));

        final ResponseEntity<Cart> response = cartController.addTocart(createModifyCartRequest());

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart cart = response.getBody();

        assertNotNull(cart);
        assertEquals(1, cart.getItems().size());
        assertEquals("jacket", cart.getItems().get(0).getName());
    }

    @Test
    public void removeFromCartTest() throws Exception {

        Mockito.when(userRepo.findByUsername("user")).thenReturn(createUser());
        Mockito.when(itemRepo.findById(1L)).thenReturn(Optional.of(createItems().get(0)));

        final ResponseEntity<Cart> response = cartController.removeFromcart(createModifyCartRequest());

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart cart = response.getBody();

        assertNotNull(cart);
        assertEquals(0, cart.getItems().size());
    }

    @Test
    public void addToCartWithInvalidUserTest() throws Exception {

        ModifyCartRequest modifyCartRequest = createModifyCartRequest();
        modifyCartRequest.setUsername("userNotFound");

        final ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void removeFromCartInvalidItemTest() throws Exception {

        ModifyCartRequest modifyCartRequest = createModifyCartRequest();
        modifyCartRequest.setItemId(55L);

        final ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    private User createUser() {

        User user = new User();

        user.setId(1L);
        user.setUsername("user");
        user.setPassword("password");
        user.setCart(new Cart());

        return user;
    }

    public List<Item> createItems() {
        return (Arrays.asList(new Item(1L, "jacket", BigDecimal.valueOf(109.99), "blue zipped jacket"),
                new Item(2L, "hat", BigDecimal.valueOf(50.00), "black elegant hat"),
                new Item(3L, "trousers", BigDecimal.valueOf(89.90), "black cotton trousers")));
    }

    public ModifyCartRequest createModifyCartRequest() {

        ModifyCartRequest cartRequest = new ModifyCartRequest();

        cartRequest.setUsername("user");
        cartRequest.setItemId(1L);
        cartRequest.setQuantity(1);

        return cartRequest;
    }
}
