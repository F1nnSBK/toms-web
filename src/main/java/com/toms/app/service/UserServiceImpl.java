package com.toms.app.service;

import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.toms.app.dto.UserDTO;
import com.toms.app.mapper.UserMapper;
import com.toms.app.model.User;
import com.toms.app.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDTO addUser(UserDTO user) {
        try {
            log.info("Creating user {}", user.getUsername());
            User tmp = this.userMapper.userDTOToUser(user);
            tmp.setId(null);
            tmp.setPassword(passwordEncoder.encode(user.getPassword()));
            return this.userMapper.userToUserDTO(this.userRepository.save(tmp));
        } catch (Exception e){
            log.error("Error saving user {}", user.getUsername(), e);
            throw new RuntimeException("Error saving user", e);
        }
    }

    public UserDTO updateUser(UserDTO updatedUser) {
        if(userRepository.existsById(updatedUser.getId())) {
            User tmp = this.userMapper.userDTOToUser(updatedUser);
            return this.userMapper.userToUserDTO(this.userRepository.save(tmp));
        } else {
            throw new EntityNotFoundException("User with id " + updatedUser.getId() + " does not exist");
        }
    }

    public List<UserDTO> findAllUsers(){
        if (userRepository.findAll().isEmpty()) {
            log.info("No users found");
        }
        return this.userMapper.usersToUserDTOs(userRepository.findAll());
    }

    public UserDTO getUserById(long id) {
        return this.userMapper.userToUserDTO(this.userRepository.findById(id).get());
    }

    public UserDTO getUserByUsername(String username) {
        return this.userMapper.userToUserDTO(this.userRepository.findByUsername(username).get());
    }

    public void removeUserById(long id) {
        this.userRepository.deleteById(id);
    }
}
