package com.toms.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toms.app.dto.ItemDTO;
import com.toms.app.service.ItemService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.NoSuchElementException;

@WebMvcTest(controllers = ItemControllerImpl.class)
@AutoConfigureMockMvc(addFilters = false)
public class ItemControllerImplTest {

    @MockBean
    ItemService itemService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    // Test Entities
    List<ItemDTO> items;
    ItemDTO item01;
    ItemDTO item02;

    // Controller Path
    private final String controllerPath = "/api/v1/items";

    @BeforeEach
    public void setup() {

        item01 = new ItemDTO(
                1L,
                "Katze",
                "description",
                "image link",
                "category",
                "quantity",
                true
        );

        item02 = new ItemDTO(
                1L,
                "Hund",
                "",
                "",
                "category",
                "",
                false
        );

        this.items = List.of(item01, item02);
    }

    @Test
    public void shouldSuccessfullyReturnFilledListOfItems() throws Exception {
        when(itemService.getAllItems()).thenReturn(items);

        this.mockMvc.perform(get(controllerPath + "/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(item01.getId().intValue())))
                .andExpect(jsonPath("$[0].name", is(item01.getName())))
                .andExpect(jsonPath("$[0].description", is(item01.getDescription())))
                .andExpect(jsonPath("$[0].image", is(item01.getImage())))
                .andExpect(jsonPath("$[0].category", is(item01.getCategory())))
                .andExpect(jsonPath("$[0].quantity", is(item01.getQuantity())))
                .andExpect(jsonPath("$[0].onlineStatus", is(item01.getOnlineStatus().booleanValue())))
                .andDo(print());

        verify(itemService).getAllItems();

    }

    @Test
    public void shouldReturnEmptyItemList() throws Exception {
        when(itemService.getAllItems()).thenReturn(List.of());

        this.mockMvc.perform(get(controllerPath + "/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(0)))
                .andDo(print());

        verify(itemService).getAllItems();
    }

    @Test
    public void shouldReturnItemById() throws Exception {
        when(itemService.getItemById(1L)).thenReturn(item01);

        this.mockMvc.perform(get(controllerPath + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(7)))
                .andExpect(jsonPath("$.id", is(item01.getId().intValue())))
                .andExpect(jsonPath("$.name", is(item01.getName())))
                .andExpect(jsonPath("$.description", is(item01.getDescription())))
                .andDo(print());

        verify(itemService).getItemById(1L);
    }

    @Test
    public void shouldFailReturnItemByIdBecauseIdNotFound() throws Exception {
        when(itemService.getItemById(anyLong())).thenThrow(new NoSuchElementException("Item by id not found"));

        this.mockMvc.perform(get(controllerPath + "/999"))
                .andExpect(status().isNotFound())
                .andDo(print());

        verify(itemService).getItemById(anyLong());
    }

    @Test
    public void shouldUpdateItemSuccessfully() throws Exception {
        when(itemService.updateItem(any())).thenReturn(item01);

        this.mockMvc.perform(put(
                controllerPath + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(item01))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(7)))
                .andExpect(jsonPath("$.id", is(item01.getId().intValue())))
                .andExpect(jsonPath("$.name", is(item01.getName())))
                .andExpect(jsonPath("$.description", is(item01.getDescription())))
                .andDo(print());

        verify(itemService).updateItem(any());
    }

    @Test
    public void shouldFailUpdateItemBecauseUnknownId() throws Exception {
        when(itemService.updateItem(any())).thenThrow(new EntityNotFoundException("Item not found by id"));

        this.mockMvc.perform(put(
                controllerPath + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(item01))
        )
                .andExpect(status().isNotFound());

        verify(itemService).updateItem(any());
    }

    @Test
    public void shouldFailUpdateItemBecauseConstraintViolation() throws Exception {
        item01.setName(null);

        this.mockMvc.perform(put(
                controllerPath + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(item01))
        )
                .andExpect(status().isBadRequest())
                .andDo(print());

        verify(itemService, never()).updateItem(any());
    }

    @Test
    public void shouldSuccessfullyDeleteItemById() throws Exception {
        doNothing().when(itemService).deleteItemById(1L);
        this.mockMvc.perform(delete(controllerPath + "/1"))
                .andExpect(status().isNoContent())
                .andDo(print());
        verify(itemService).deleteItemById(anyLong());
    }

    @Test
    public void shouldNotDeleteItemByIdBecauseIdNotFound() throws Exception {
        doThrow(new NoSuchElementException("Item not found by id")).when(itemService).deleteItemById(1L);
        this.mockMvc.perform(delete(controllerPath + "/1"))
                .andExpect(status().isNotFound())
                .andDo(print());
        verify(itemService).deleteItemById(anyLong());
    }

    @Test
    public void shouldSuccessfullyAddItem() throws Exception {
        when(itemService.addItem(any())).thenReturn(item01);
        this.mockMvc.perform(post(controllerPath + "/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(item01))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(7)))
                .andExpect(jsonPath("$.id", is(item01.getId().intValue())))
                .andExpect(jsonPath("$.name", is(item01.getName())))
                .andDo(print());
        verify(itemService).addItem(any());
    }

    @Test
    public void shouldFailAddItemBecauseConstraintViolation() throws Exception {
        item01.setName(null);

        this.mockMvc.perform(post(controllerPath + "/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(item01))
        )
                .andExpect(status().isBadRequest())
                .andDo(print());
        verify(itemService, never()).addItem(any());
    }

}
