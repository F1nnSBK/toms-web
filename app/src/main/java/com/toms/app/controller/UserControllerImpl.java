package com.toms.app.controller;

import com.toms.app.dto.UserDTO;
import com.toms.app.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/users")
public class UserControllerImpl implements UserController {

    private final UserService userService;

    public UserControllerImpl(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public List<UserDTO> findAllUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("/{userId}")
    public UserDTO getUserById(@PathVariable("userId") Long id) {
        return this.userService.getUserById(id);
    }

    @PostMapping("/")
    public UserDTO addUser(@RequestBody UserDTO user) {
        try {
            return userService.addUser(user);
        } catch (Exception e){
            throw new RuntimeException("Error saving user " + user.getUsername(), e);
        }

    }

    @DeleteMapping("/{userId}")
    public void removeUserById(@PathVariable("userId") Long id) {
        this.userService.removeUserById(id);
    }

    @PutMapping("/{userId}")
    public UserDTO updateUser(@PathVariable("userId") Long userId, @RequestBody UserDTO user) {
        user.setId(userId);
        try {
            return userService.updateUser(user);
        } catch (EntityNotFoundException notFoundException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } catch (
                NoSuchElementException | ConstraintViolationException noSuchElementException) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

}
