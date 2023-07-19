//package com.team3.ecommerce.controller;
//
//import com.team3.ecommerce.entity.Customer;
//import com.team3.ecommerce.repository.CustomerRepository;
//import com.team3.ecommerce.service.CustomerService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/home")
//public class LoginCustomerController {
//    @Autowired
//    CustomerService customerService;
//    @PostMapping("/login")
//    public ResponseEntity<?> LoginCustomer(@RequestBody Customer customer) {
//        Customer customer1 = customerService.findCustomerByEmail(customer.getEmail());
//        if (customer1 != null && customer1.getPassword().equals(customer.getPassword())) {
//            return new ResponseEntity<>(customer1, HttpStatus.ACCEPTED);
//        } else {
//            return ResponseEntity.badRequest().body("Wrong Email or Password");
//        }
//    }
//}
