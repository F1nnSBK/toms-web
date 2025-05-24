package com.toms.app.service;

import com.toms.app.dto.UserDTO;
import com.toms.app.mapper.UserMapper;
import com.toms.app.model.User;
import com.toms.app.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserDTO addUser(UserDTO user) {
        try {
            log.info("Creating user {}", user.getUsername());
            User tmp = this.userMapper.userDTOToUser(user);
            tmp.setId(null);
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
            log.info("No users found, creating default user");
            addUser(this.userMapper.userToUserDTO(
                    new User(
                    "admin",
                    "admin",
                    "<EMAIL>",
                    "ROLE_ADMIN",
                    "0123456789")
            ));
        }
        return this.userMapper.usersToUserDTOs(userRepository.findAll());
    }

    public UserDTO getUserById(long id) {
        return this.userMapper.userToUserDTO(this.userRepository.findById(id).get());
    }

    public void removeUserById(long id) {
        this.userRepository.deleteById(id);
    }
}
