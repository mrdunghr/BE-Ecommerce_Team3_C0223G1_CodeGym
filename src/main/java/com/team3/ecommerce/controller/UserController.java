package com.team3.ecommerce.controller;

import com.team3.ecommerce.entity.User;
import com.team3.ecommerce.service.AccountUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired
    private AccountUserService accountUserService;

    // đăng nhập user
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        User user1 = accountUserService.findUserByEmail(user.getEmail());
        if (user1 != null && user1.getPassword().equals(user.getPassword())) {
            return new ResponseEntity<>(user1, HttpStatus.ACCEPTED);
        } else {
            return ResponseEntity.badRequest().body("Wrong Email or Password");
        }
    }
    @PostMapping("/create-user")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        User newUser = accountUserService.findUserByEmail(user.getEmail());
        if(newUser != null) {
            return ResponseEntity.badRequest().body("Email already exists");
        } else {
            return new ResponseEntity<>(accountUserService.createUser(user),HttpStatus.OK);
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<User> findUserById(@PathVariable Integer id){
        return new ResponseEntity<>(accountUserService.findUserById(id),HttpStatus.OK);
    }
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Integer id, @RequestBody User updatedUser) {
        User existingUser = accountUserService.findUserById(id);
        if (existingUser == null) {
            return ResponseEntity.notFound().build();
        }
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setPassword(updatedUser.getPassword());
        existingUser.setEnabled(updatedUser.isEnabled());
        existingUser.setPhotos(updatedUser.getPhotos());
        existingUser.setRoles(updatedUser.getRoles());

        updatedUser = accountUserService.createUser(existingUser);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }
}
