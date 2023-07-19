package com.team3.ecommerce.controller;

import com.team3.ecommerce.entity.Customer;
import com.team3.ecommerce.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home")
public class LoginCustomerController {
    @Autowired
    CustomerRepository customerRepository;
    @PostMapping("/login")
    public ResponseEntity<String> LoginCustomer(@RequestBody Customer customer) {
        Customer customer1 = customerRepository.findByEmail(customer.getEmail());
        if (customer1 != null && customer1.getPassword().equals(customer.getPassword())) {
            return new ResponseEntity<>("Đăng nhập thành công", HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>("Sai Email hoặc mật Khẩu", HttpStatus.BAD_REQUEST);
        }
    }
}
