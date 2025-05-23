package com.toms.app.controller;

import com.toms.app.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface UserController {

    @PostMapping("/")
    String addUser(@RequestBody User user);

    @GetMapping("/")
    List<User> findAllUsers();
}
