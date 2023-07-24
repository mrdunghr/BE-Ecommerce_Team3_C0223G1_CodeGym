package com.team3.ecommerce.controller;

import com.team3.ecommerce.entity.Country;
import com.team3.ecommerce.entity.Customer;
import com.team3.ecommerce.entity.Shop;
import com.team3.ecommerce.service.CustomerService;
import com.team3.ecommerce.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    @Autowired
    private ShopService shopService;

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

    // lấy dánh sách shop của customer
    @GetMapping("/{id}/shops")
    public ResponseEntity<?> findShopByCustomer(@PathVariable Integer id,
                                                         @RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "5") int size) {
        Customer customer = customerService.getCustomerById(id).orElse(null);
        if (customer != null && customer.isEnabled()) {
            Pageable pageable = PageRequest.of(page, size);
            Page<Shop> shops = shopService.findShopByCustomer(customer, pageable);
            return new ResponseEntity<>(shops, HttpStatus.OK);
        } else {
            return ResponseEntity.badRequest().body("tài khoản không tồn tại hoặc bị vô hiệu hóa");
        }
    }
}
