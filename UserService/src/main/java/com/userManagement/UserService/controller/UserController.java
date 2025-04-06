package com.userManagement.UserService.controller;

import com.userManagement.UserService.Entity.User;
import com.userManagement.UserService.ExceptionHandler.ResourceNotFoundException;
import com.userManagement.UserService.ExceptionHandler.UserDoesNotMatchException;
import com.userManagement.UserService.service.UserService;
import com.userManagement.UserService.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user){
        return ResponseEntity.ok(userService.registerUser(user));
    }

    @GetMapping("/getUserDetails")
    public ResponseEntity<Optional<User>> getUserDetails(@RequestHeader("Authorization") String authHeader){
        String token = authHeader.substring(7); // Removing "Bearer "
        String username = jwtUtil.extractUsername(token);
        return ResponseEntity.ok(userService.getUserDetails(username));
    }

    @PutMapping("/UpdateUserDetails")
    public ResponseEntity<User> updateUserDetails(@RequestHeader("Authorization") String authHeader, @RequestBody User updatedUser) throws UserDoesNotMatchException {
        String token = authHeader.substring(7);
        String username = jwtUtil.extractUsername(token);
        if(!Objects.equals(username, updatedUser.getUsername())){
            throw new UserDoesNotMatchException("Current User does not match");
        }
        return ResponseEntity.ok(userService.updateUserDetails(username,updatedUser));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/updateUser/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) throws ResourceNotFoundException {
        return ResponseEntity.ok(userService.updateUser(id, updatedUser));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) throws ResourceNotFoundException {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }


}
