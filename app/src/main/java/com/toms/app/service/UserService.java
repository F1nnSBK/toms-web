package com.toms.app.service;

import com.toms.app.model.User;

import java.util.List;

public interface UserService {
    void addUser(
            String username,
            String password,
            String email,
            String role,
            String phone);

    List<User> findAllUsers();
}
