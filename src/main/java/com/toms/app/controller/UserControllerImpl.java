package com.toms.app.controller;

import com.toms.app.dto.UserDTO;
import com.toms.app.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
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

    @GetMapping("/id/{userId:\\d+}")
    public UserDTO getUserById(@PathVariable("userId") Long id) {
        try {
            return this.userService.getUserById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/{userName}")
    public UserDTO getUserByUsername(@PathVariable("userName") String userName) {
        try {
            return this.userService.getUserByUsername(userName);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO addUser(@Valid @RequestBody UserDTO user) {
        try {
            return userService.addUser(user);
        } catch (ConstraintViolationException cve){
            throw new RuntimeException("Error saving user " + user.getUsername(), cve);
        }

    }

    @DeleteMapping("/id/{userId:\\d+}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeUserById(@PathVariable("userId") Long id) {
        try{
            this.userService.removeUserById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/id/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public UserDTO updateUser(@PathVariable("userId") Long userId, @Valid @RequestBody UserDTO user) {
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
