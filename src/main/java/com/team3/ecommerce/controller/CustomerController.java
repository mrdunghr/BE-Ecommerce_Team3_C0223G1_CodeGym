package com.team3.ecommerce.controller;

import com.team3.ecommerce.entity.ChangePasswordRequest;
import com.team3.ecommerce.entity.Country;
import com.team3.ecommerce.entity.Customer;
import com.team3.ecommerce.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/customers")
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
    public ResponseEntity<List<Country>> getCountryList(){
        return new ResponseEntity<>(customerService.listAllCountries(), HttpStatus.OK);
    }

    //đổi mật khẩu của của Customer
    @PutMapping("/edit-password/{idCustomer}")
    public ResponseEntity<?> editPassword(@PathVariable Integer idCustomer, @RequestBody ChangePasswordRequest request){
        Optional<Customer> customer1 = customerService.findById(idCustomer);
        if(!customer1.isPresent()){
            return ResponseEntity.notFound().build();
        }
        if (!customer1.get().getPassword().equals(request.getOldPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect old password");
        }
        customer1.get().setPassword(request.getNewPassword());
        customerService.saveCustomer(customer1.get());
        return ResponseEntity.ok("Password changed successfully");
    }
}
