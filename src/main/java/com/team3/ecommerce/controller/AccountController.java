package com.team3.ecommerce.controller;

import com.team3.ecommerce.entity.Customer;
import com.team3.ecommerce.entity.User;
import com.team3.ecommerce.service.AccountCustomerService;
import com.team3.ecommerce.service.AccountUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/account")
public class AccountController {

    @Autowired
    private AccountCustomerService accountCustomerService;

    @Autowired
    private AccountUserService accountUserService;


    @GetMapping("/customers/list")
    public ResponseEntity<Page<Customer>> listCustomer(@RequestParam(name = "page", defaultValue = "0") int page,
                                                       @RequestParam(name = "size", defaultValue = "3") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Customer> listCustomer1 = accountCustomerService.listCustomer(pageable);
        return new ResponseEntity<>(listCustomer1, HttpStatus.OK);
    }

    // hiển thị tất cả tài khoản User
    @GetMapping("/users/list")
    public ResponseEntity<Page<User>> listUser(@RequestParam(name = "page", defaultValue = "0") int page,
                                               @RequestParam(name = "size", defaultValue = "3") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> listUser1 = accountUserService.listUsers(pageable);
        return new ResponseEntity<>(listUser1, HttpStatus.OK);
    }
}
