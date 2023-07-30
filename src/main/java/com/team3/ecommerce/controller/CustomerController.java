package com.team3.ecommerce.controller;

import com.team3.ecommerce.entity.Country;
import com.team3.ecommerce.entity.Customer;
import com.team3.ecommerce.entity.User;
import com.team3.ecommerce.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/customers")
@CrossOrigin("*")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Integer id) {
        Optional<Customer> customer = customerService.getCustomerById(id);
        if (customer.isPresent()) {
            return ResponseEntity.ok(customer.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/check-email")
    public ResponseEntity<Boolean> isEmailUnique(@RequestParam String email) {
        boolean isUnique = customerService.isEmailUnique(email);
        return ResponseEntity.ok(isUnique);
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerCustomer(@RequestBody Customer customer) {
        boolean isUnique = customerService.isEmailUnique(customer.getEmail());
        if (!isUnique) {
            return ResponseEntity.badRequest().body("Email already exists.");
        }

        customerService.registerCustomer(customer);
        return ResponseEntity.ok("Registration successful.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> LoginCustomer(@RequestBody Customer customer) {
        Customer customer1 = customerService.findCustomerByEmail(customer.getEmail());
        if (customer1 != null && customer1.getPassword().equals(customer.getPassword())) {
            return new ResponseEntity<>(customer1, HttpStatus.ACCEPTED);
        } else {
            return ResponseEntity.badRequest().body("Wrong Email or Password");
        }
    }

    @GetMapping("/list-country")
    public ResponseEntity<List<Country>> getCountryList() {
        return new ResponseEntity<>(customerService.listAllCountries(), HttpStatus.OK);
    }
    @PutMapping("/{id}/enabled/{status}")
    public ResponseEntity<String> updateUserEnabledStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean enabled) {
        try {
            customerService.updateUserEnabledStatus(id, enabled);
            String status = enabled ? "enabled" : "disabled";
            String message = "The customer ID " + id + " has been " + status;
            return ResponseEntity.ok(message);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        Customer existingCustomer = customerService.findById(id).get();
        if (existingCustomer == null) {
            return ResponseEntity.notFound().build();
        }
        customerService.deleteCustomerById(id);
        return ResponseEntity.ok("Customer deleted successfully.");
    }

}
