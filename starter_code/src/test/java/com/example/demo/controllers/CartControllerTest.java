package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class CartControllerTest {

    @InjectMocks
    CartController cartController;

    @Mock
    UserRepository userRepository;

    @Mock
    ItemRepository itemRepository;

    @Mock
    CartRepository cartRepository;

    @Test
    void addToCartNotFoundUser() {
        Mockito.doReturn(null)
                .when(userRepository).findByUsername(Mockito.any());
        ResponseEntity<Cart> responseEntity = cartController.addTocart(new ModifyCartRequest());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void addToCartNotFoundItem() {
        Mockito.doReturn(new User()).when(userRepository).findByUsername(Mockito.any());
        Mockito.doReturn(Optional.empty()).when(itemRepository).findById(Mockito.any());
        ResponseEntity<Cart> responseEntity = cartController.addTocart(new ModifyCartRequest());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void addToCartSuccess() {
        User user = createUser();
        Cart cart = createCart();
        Item item = createItem();
        List<Item> items = new ArrayList<>();
        items.add(item);
        cart.setUser(user);
        cart.setItems(items);
        user.setCart(cart);

        ModifyCartRequest request = new ModifyCartRequest();
        request.setQuantity(3);
        request.setItemId(item.getId());
        request.setUsername(user.getUsername());

        Mockito.doReturn(user).when(userRepository).findByUsername(Mockito.any());
        Mockito.doReturn(Optional.of(item)).when(itemRepository).findById(Mockito.any());

        ResponseEntity<Cart> responseEntity = cartController.addTocart(request);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(cart, responseEntity.getBody());
    }

    @Test
    void removeFromCartNotFoundUser(){
        Mockito.doReturn(null).when(userRepository)
                .findByUsername(Mockito.any());
        ResponseEntity<Cart> responseEntity = cartController.removeFromcart(new ModifyCartRequest());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void removeFromCartNotFoundItem(){
        Mockito.doReturn(new User()).when(userRepository).findByUsername(Mockito.any());
        Mockito.doReturn(Optional.empty()).when(itemRepository)
                .findById(Mockito.any());
        ResponseEntity<Cart> responseEntity = cartController
                .removeFromcart(new ModifyCartRequest());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void removeFormCartSuccess(){
        User user = createUser();
        Cart cart = createCart();
        Item item = createItem();
        List<Item> items = new ArrayList<>();
        items.add(item);
        cart.setUser(user);
        cart.setItems(items);
        user.setCart(cart);

        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername(user.getUsername());
        request.setQuantity(2);
        request.setItemId(1L);
        Mockito.doReturn(user).when(userRepository).findByUsername(Mockito.any());
        Mockito.doReturn(Optional.of(item)).when(itemRepository)
                .findById(Mockito.any());

        ResponseEntity<Cart> responseEntity = cartController.removeFromcart(request);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(0, responseEntity.getBody().getItems().size());

    }

    private User createUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("thangtq21");
        user.setPassword("12345678");
        return user;
    }

    private Cart createCart() {
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setTotal(BigDecimal.ONE);
        return cart;
    }

    private Item createItem() {
        Item item = new Item();
        item.setId(1L);
        item.setName("item1");
        item.setPrice(BigDecimal.ONE);
        item.setDescription("item 1 with good condition and quality");
        return item;
    }
}
