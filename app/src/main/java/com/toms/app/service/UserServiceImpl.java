package com.toms.app.service;

import com.toms.app.model.User;
import com.toms.app.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void addUser(
            String username,
            String password,
            String email,
            String role,
            String phone) {
        try {
            userRepository.save(
                    new User(username, password, email, role, phone)
            );
            log.info("User saved successfully {}", username);
        } catch (Exception e){
            log.error("Error saving user", e);
            throw new RuntimeException("Error saving user", e);
        }
    }

    public List<User> findAllUsers(){
        if (userRepository.findAll().isEmpty()) {
            log.info("No users found, creating default user");
            addUser("admin", "admin", "<EMAIL>", "ROLE_ADMIN", "0123456789");
        }
        return userRepository.findAll();
    }
}
