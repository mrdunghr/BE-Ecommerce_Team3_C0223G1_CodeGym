package com.team3.ecommerce.controller;

import com.team3.ecommerce.entity.Country;
import com.team3.ecommerce.entity.Customer;
import com.team3.ecommerce.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
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
    public ResponseEntity<String> LoginCustomer(@RequestBody Customer customer, HttpSession session) {
        Customer customerCheckLogin = customerService.findCustomerByEmail(customer.getEmail());
        if (customerCheckLogin != null && customerCheckLogin.getPassword().equals(customer.getPassword())) {
            // Đăng nhập thành công, lưu thông tin khách hàng vào session
            session.setAttribute("customerId", customerCheckLogin.getId());
            return ResponseEntity.ok().body("Success");
        } else {
            return ResponseEntity.badRequest().body("Wrong Email or Password");
        }
    }


    @GetMapping("/list-country")
    public ResponseEntity<List<Country>> getCountryList() {
        return new ResponseEntity<>(customerService.listAllCountries(), HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        // Xóa thông tin khách hàng trong session để đăng xuất
        session.removeAttribute("customerId");
        return ResponseEntity.ok("Logout successful");
    }

}
