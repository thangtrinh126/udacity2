package com.example.demo.controllers;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
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

@ExtendWith(MockitoExtension.class)
public class ItemControllerTest {

    @InjectMocks
    ItemController itemController;

    @Mock
    ItemRepository itemRepository;

    @Test
    void getItems() {
        Mockito.doReturn(new ArrayList<Item>())
                .when(itemRepository).findAll();
        ResponseEntity<List<Item>> responseEntity = itemController.getItems();
        assertEquals(0, responseEntity.getBody().size());
    }

    @Test
    void getItemsByNameEmptyList() {
        Mockito.doReturn(new ArrayList<Item>()).when(itemRepository)
                .findByName("Item2");
        ResponseEntity<List<Item>> responseEntity = itemController.getItemsByName("Item2");
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void getItemsByNameNull(){
        Mockito.doReturn(null)
                .when(itemRepository)
                .findByName("item2");
        ResponseEntity<List<Item>> responseEntity
                = itemController.getItemsByName("item2");
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void getItemsByNameContainsData(){
        List<Item> items = new ArrayList<>();
        Item item = new Item();
        item.setId(1L);
        item.setName("Item-1");
        item.setPrice(BigDecimal.ONE);
        item.setDescription("hahahehe");
        items.add(item);
        Mockito.doReturn(items).when(itemRepository).findByName("Item-1");
        ResponseEntity<List<Item>> responseEntity = itemController.getItemsByName("Item-1");
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

    }

}
