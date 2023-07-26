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
    public ResponseEntity<?> LoginUser(@RequestBody User user) {
        User user1 = accountUserService.findUserByEmail(user.getEmail());
        if (user1 != null && user1.getPassword().equals(user.getPassword())) {
            return new ResponseEntity<>(user1, HttpStatus.ACCEPTED);
        } else {
            return ResponseEntity.badRequest().body("Wrong Email or Password");
        }
    }
    @PostMapping("create-user")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        User newUser = accountUserService.findUserByEmail(user.getEmail());
        if(newUser != null) {
            return ResponseEntity.badRequest().body("Email already exists");
        } else {
            return new ResponseEntity<>(accountUserService.createUser(user),HttpStatus.OK);
        }
    }
}
