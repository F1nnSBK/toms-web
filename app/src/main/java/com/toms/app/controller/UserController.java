package com.toms.app.controller;

import com.toms.app.dto.UserDTO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface UserController {

    @PutMapping("/{userId}")
    UserDTO updateUser(@PathVariable("userId") Long userId, @Valid @RequestBody UserDTO user);

    @PostMapping("/")
    UserDTO addUser(@Valid @RequestBody UserDTO user);

    @GetMapping("/")
    List<UserDTO> findAllUsers();

    @GetMapping("/{userId}")
    UserDTO getUserById(@PathVariable("userId") Long userId);

    @GetMapping("/{userName}")
    public UserDTO getUserByUsername(@PathVariable("userName") String userName);

    @DeleteMapping("/{userId}")
    void removeUserById(@PathVariable("userId") Long userId);
}
