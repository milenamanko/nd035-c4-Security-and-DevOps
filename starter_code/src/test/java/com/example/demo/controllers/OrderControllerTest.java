package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    OrderController orderController;

    @Mock
    private UserRepository userRepo;

    @Mock
    private OrderRepository orderRepo;

    @BeforeEach
    public void setup() {

        MockitoAnnotations.initMocks(this);

        orderController = new OrderController();

        TestUtils.injectObjects(orderController, "userRepository", userRepo);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepo);

    }

    @Test
    public void submitOrderTest() throws Exception {

        when(userRepo.findByUsername("user")).thenReturn(createUser());

        final ResponseEntity<UserOrder> response = orderController.submit("user");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        UserOrder order = response.getBody();
        assert order != null;
        assertEquals(BigDecimal.valueOf(249.89), order.getTotal());
    }

    @Test
    public void submitOrderForInvalidUserTest() throws Exception {

        final ResponseEntity<UserOrder> response = orderController.submit("userNotFound");

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

        UserOrder order = response.getBody();

        Assertions.assertNull(order);
    }

    @Test
    public void getOrdersForUserTest() throws Exception {

        String testUserName = "user";

        User user = createUser();
        when(userRepo.findByUsername(testUserName)).thenReturn(user);
        when(orderRepo.findByUser(user)).thenReturn(createUserOrder());

        final ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(testUserName);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<UserOrder> userOrders = response.getBody();
        assert userOrders != null;
        assertEquals(1, userOrders.size());
        assertEquals(3, userOrders.get(0).getItems().size());
    }

//    @Test
//    public void getOrdersForInvalidUserTest() throws Exception {
//
//    }


    private User createUser() {

        User user = new User();

        user.setId(1L);
        user.setUsername("user");
        user.setPassword("password");

        Cart cart = new Cart();

        cart.setId(1L);
        cart.setItems(Arrays.asList(new Item(1L, "jacket", BigDecimal.valueOf(109.99), "blue zipped jacket"),
                new Item(2L, "hat", BigDecimal.valueOf(50.00), "black elegant hat"),
                new Item(3L, "trousers", BigDecimal.valueOf(89.90), "black cotton trousers")));
        cart.setUser(user);
        cart.setTotal(BigDecimal.valueOf(249.89));

        user.setCart(cart);

        return user;
    }

    private List<UserOrder> createUserOrder() {
        UserOrder userOrder = UserOrder.createFromCart(createUser().getCart());
        return Lists.list(userOrder);
    }
}
