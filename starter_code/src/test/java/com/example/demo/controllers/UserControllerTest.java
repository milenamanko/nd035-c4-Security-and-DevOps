package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    private UserController userController;

    @Mock
    private UserRepository userRepo;

    @Mock
    private CartRepository cartRepo;

    @Mock
    private BCryptPasswordEncoder encoder;

    @Before
    public void setup() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepo);
        TestUtils.injectObjects(userController, "cartRepository", cartRepo);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void createUserHappyPathTest() throws Exception {

        Mockito.when(encoder.encode("password")).thenReturn("hashedPass");

        CreateUserRequest request = new CreateUserRequest();

        request.setUsername("user");
        request.setPassword("password");
        request.setConfirmPassword("password");

        final ResponseEntity<User> response = userController.createUser(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();

        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("user", user.getUsername());
        assertEquals("hashedPass", user.getPassword());

    }

    @Test
    public void createUserWithWrongPassTest() throws Exception {

        CreateUserRequest request = new CreateUserRequest();

        request.setUsername("user");
        request.setPassword("pass1");
        request.setConfirmPassword("pass1");

        final ResponseEntity<User> response = userController.createUser(request);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void getUserByIdHappyPathTest() throws Exception {

        Mockito.when(userRepo.findById(1L)).thenReturn(Optional.of(createUser()));

        final ResponseEntity<User> response = userController.findById(1L);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();

        assertNotNull(user);
        assertEquals(1, user.getId());
        assertEquals("user", user.getUsername());
        assertEquals("password", user.getPassword());
    }

    @Test
    public void getUserByNameHappyPathTest() throws Exception {
        
        final ResponseEntity<User> response = userController.findByUserName("userNotFound");

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

        User user = response.getBody();

        assertNull(user);
    }

    private User createUser() {

        User user = new User();

        user.setId(1L);
        user.setUsername("user");
        user.setPassword("password");

        return user;
    }
}
