package com.toms.app.service;

import com.toms.app.dto.UserDTO;


import java.util.List;

public interface UserService {
    UserDTO addUser(UserDTO user);
    UserDTO updateUser(UserDTO updatedUser);
    List<UserDTO> findAllUsers();
    UserDTO getUserById(long id);
    void removeUserById(long id);
}
