package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Matchers.any;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    @InjectMocks
    OrderController orderController;

    @Mock
    UserRepository userRepository;

    @Mock
    OrderRepository orderRepository;

    @Test
    void submitNotFoundUser(){
        Mockito.doReturn(null)
                .when(userRepository)
                .findByUsername("thang");
        ResponseEntity<UserOrder> responseEntity = orderController.submit("thang");
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void submitSuccess(){
        User user = createUser();
        Cart cart = createCart();
        user.setCart(cart);

        Mockito.doReturn(user)
                .when(userRepository)
                .findByUsername("thang");

        ResponseEntity<UserOrder> responseEntity = orderController.submit("thang");
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void getOrdersForUserNotFound(){
        Mockito.doReturn(null)
                .when(userRepository)
                .findByUsername("thang");
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("thang");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getOrdersForUserSuccess(){
        Mockito.doReturn(new User()).when(userRepository).findByUsername("thang");
        Mockito.doReturn(new ArrayList<User>()).when(orderRepository).findByUser(any());
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("thang");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    private User createUser(){
        User user = new User();
        user.setId(1L);
        user.setUsername("thang");
        user.setPassword("12345678");
        return user;
    }

    private Cart createCart(){
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setTotal(BigDecimal.ONE);

        Item item = new Item();
        item.setId(1L);
        item.setName("Item1");
        item.setPrice(BigDecimal.ONE);
        item.setDescription("hihihaha");

        List<Item> items = new ArrayList<>();
        items.add(item);

        cart.setItems(items);
        return cart;

    }
}
