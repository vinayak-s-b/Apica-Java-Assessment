package com.userManagement.UserService.service;

import com.userManagement.UserService.Entity.User;
import com.userManagement.UserService.ExceptionHandler.ResourceNotFoundException;
import com.userManagement.UserService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private  KafkaTemplate<String, String> kafkaTemplate;

    public User registerUser(User user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        // Publish user creation event to Kafka
        kafkaTemplate.send("user-events", "User created: " + savedUser.getUsername());
        return savedUser;
    }

    public List<User> getAllUsers() {

        //  publish an update event
        kafkaTemplate.send("user-events", "Fetching all users");

        return userRepository.findAll();
    }

    public Optional<User> getUserDetails(String username){

        //  publish an update event
        kafkaTemplate.send("user-events", "getting details of : " + username);

        return userRepository.findByUsername(username);
    }

    public User updateUserDetails(String username,User updatedUser){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Update fields (except role)
        user.setUsername(updatedUser.getUsername());
        user.setEmail(updatedUser.getEmail());
        // update password if it's changed
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            user.setPassword(new BCryptPasswordEncoder().encode(updatedUser.getPassword()));
        }
        kafkaTemplate.send("user-events", "User updated: " + user.getUsername());

        return userRepository.save(user);

    }

    public User updateUser(Long id, User updatedUser) throws ResourceNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

        user.setUsername(updatedUser.getUsername());
        user.setEmail(updatedUser.getEmail());

        // update password if it's changed
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            user.setPassword(new BCryptPasswordEncoder().encode(updatedUser.getPassword()));
        }

        user.setRole(updatedUser.getRole());

        //  publish an update event
        kafkaTemplate.send("user-events", "User updated: " + user.getUsername());

        return userRepository.save(user);
    }
    public void deleteUser(Long id) throws ResourceNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

        userRepository.delete(user);

        //  publish a delete event
        kafkaTemplate.send("user-events", "User deleted: " + user.getUsername());
    }


}