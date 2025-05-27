package com.toms.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toms.app.dto.UserDTO;
 import com.toms.app.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import java.util.List;

@WebMvcTest(controllers = UserControllerImpl.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerImplTest {

    @MockBean
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    List<UserDTO> users;
    UserDTO user01;
    UserDTO user02;

    private final String controllerPath = "/api/v1/users";

    @BeforeEach
    public void setup() {
        user01 = new UserDTO(
                1L,
                "user1",
                "password",
                "a@email.com",
                "ADMIN",
                "72672936932"
        );

        user02 = new UserDTO(
                2L,
                "user2",
                "password",
                "a@email.com",
                "USER",
                "72672936932"
        );

        users = List.of(user01, user02);
    }

    @Test
    public void shouldSuccessfullyGetAllUsers() throws Exception {
        when(userService.findAllUsers()).thenReturn(users);
        this.mockMvc.perform(get(controllerPath + "/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(user01.getId().intValue())))
                .andExpect(jsonPath("$[0].username", is(user01.getUsername())))
                .andExpect(jsonPath("$[0].password", is(user01.getPassword())))
                .andDo(print());
        verify(userService).findAllUsers();
    }

    @Test
    public void shouldFailGetAllUsers() throws Exception {
        when(userService.findAllUsers()).thenReturn(List.of());
        this.mockMvc.perform(get(controllerPath + "/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(0)))
                .andDo(print());
        verify(userService).findAllUsers();
    }

    @Test
    public void shouldGetUserById() throws Exception {
        when(userService.getUserById(anyLong())).thenReturn(user01);
        this.mockMvc.perform(get(controllerPath + "/id/" + user01.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user01.getId().intValue())))
                .andExpect(jsonPath("$.username", is(user01.getUsername())))
                .andExpect(jsonPath("$.password", is(user01.getPassword())))
                .andDo(print());
        verify(userService).getUserById(anyLong());
    }

    @Test
    public void shouldFailGetUserById() throws Exception {
        when(userService.getUserById(anyLong())).thenThrow(new EntityNotFoundException("No user found by id: " + user01.getId()));
        this.mockMvc.perform(get(controllerPath + "/id/" + user01.getId()))
                .andExpect(status().isNotFound())
                .andDo(print());
        verify(userService).getUserById(anyLong());
    }

    @Test
    public void shouldSuccessfullyGetUserByUsername() throws Exception {
        when(userService.getUserByUsername(anyString())).thenReturn(user01);
        this.mockMvc.perform(get(controllerPath + "/" + user01.getUsername()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(user01.getId().intValue())))
                .andExpect(jsonPath("$.username", is(user01.getUsername())))
                .andExpect(jsonPath("$.password", is(user01.getPassword())))
                .andDo(print());
        verify(userService).getUserByUsername(anyString());
    }

    @Test
    public void shouldFailGetUserByUsername() throws Exception {
        when(userService.getUserByUsername(anyString())).thenThrow(new EntityNotFoundException("User by username not found"));
        this.mockMvc.perform(get(controllerPath + "/" + user01.getUsername()))
                .andExpect(status().isNotFound())
                .andDo(print());
        verify(userService).getUserByUsername(anyString());
    }

    @Test
    public void shouldSuccesfullyAddUser() throws Exception {
        when(userService.addUser(any())).thenReturn(user01);
        this.mockMvc.perform(post(controllerPath + "/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user01)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(user01.getId().intValue())))
                .andExpect(jsonPath("$.username", is(user01.getUsername())))
                .andExpect(jsonPath("$.password", is(user01.getPassword())))
                .andDo(print());
        verify(userService).addUser(any());
    }

    @Test
    public void shouldFailAddUserBecauseConstraintViolation() throws Exception {
        user01.setUsername(null);
        this.mockMvc.perform(post(controllerPath + "/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user01)))
                .andExpect(status().isBadRequest())
                .andDo(print());
        verify(userService, never()).addUser(any());
    }

    @Test
    public void shouldSuccessfullyRemoveUser() throws Exception {
        doNothing().when(userService).removeUserById(anyLong());
        this.mockMvc.perform(delete(controllerPath + "/id/" + user01.getId()))
                .andExpect(status().isNoContent())
                .andDo(print());
        verify(userService).removeUserById(anyLong());
    }

    @Test
    public void shouldFailRemoveUserBecauseEntityNotFound() throws Exception {
        doThrow(new EntityNotFoundException("User not found")).when(userService).removeUserById(anyLong());
        this.mockMvc.perform(delete(controllerPath + "/id/" + user01.getId()))
                .andExpect(status().isNotFound())
                .andDo(print());
        verify(userService).removeUserById(anyLong());
    }

    @Test
    public void shouldSuccessfullyUpdateUser() throws Exception {
        when(userService.updateUser(any())).thenReturn(user01);
        this.mockMvc.perform(put(controllerPath + "/id/" + user01.getId())
        .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user01)))
                .andExpect(status().isNoContent())
                .andDo(print());
        verify(userService).updateUser(any());
    }

    @Test
    public void shouldFailUpdateUserBecauseConstraintViolation() throws Exception {
        user01.setUsername(null);
        this.mockMvc.perform(put(controllerPath + "/id/" + user01.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user01)))
                .andExpect(status().isBadRequest())
                .andDo(print());
        verify(userService, never()).updateUser(any());
    }

    @Test
    public void shouldFailUpdateUserBecauseEntityNotFound() throws Exception {
        doThrow(new EntityNotFoundException("User not found")).when(userService).updateUser(any());
        this.mockMvc.perform(put(controllerPath + "/id/" + user01.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user01)))
                .andExpect(status().isNotFound())
                .andDo(print());
        verify(userService).updateUser(any());
    }

}
