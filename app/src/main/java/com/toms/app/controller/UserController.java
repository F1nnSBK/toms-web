package com.toms.app.controller;

import com.toms.app.dto.UserDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface UserController {

    @PutMapping("/{userId}")
    UserDTO updateUser(@PathVariable("userId") Long userId, @RequestBody UserDTO user);

    @PostMapping("/")
    UserDTO addUser(@RequestBody UserDTO user);

    @GetMapping("/")
    List<UserDTO> findAllUsers();

    @GetMapping("/{userId}")
    UserDTO getUserById(@PathVariable("userId") Long userId);

    @DeleteMapping("/{userId}")
    void removeUserById(@PathVariable("userId") Long userId);
}
