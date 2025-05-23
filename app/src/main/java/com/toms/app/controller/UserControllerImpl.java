package com.toms.app.controller;

import com.toms.app.model.User;
import com.toms.app.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserControllerImpl implements UserController {

    private final UserService userService;

    public UserControllerImpl(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public List<User> findAllUsers() {
        return userService.findAllUsers();
    }

    @PostMapping("/")
    public String addUser(@RequestBody User user) {
        try {
            userService.addUser(
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.getRole(),
                user.getPhone());
            return "User " + user.getUsername() + " saved successfully!";
        } catch (Exception e){
            throw new RuntimeException("Error saving user " + user.getUsername(), e);
        }

    }

}
