package com.team3.ecommerce.controller;

import com.team3.ecommerce.entity.ChangePasswordRequest;
import com.team3.ecommerce.entity.Country;
import com.team3.ecommerce.entity.Customer;
import com.team3.ecommerce.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
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

    @GetMapping("/do-activation")
    public ResponseEntity<String> activateUsers(@RequestParam String code) {
        // Tìm người dùng bằng mã xác nhận và kích hoạt tài khoản (nếu chưa kích hoạt)
        Customer customer = customerService.activateCustomer(code);

        if (customer != null && customer.isEnabled()) {
            String success = "Kích hoạt tài khoản thành công.";
            return ResponseEntity.status(HttpStatus.OK).body(success);
        } else {
            String error = "Mã xác nhận không hợp lệ hoặc tài khoản đã được kích hoạt trước đó.";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> LoginCustomer(@RequestBody Customer customer) {
        Customer customerCheckLogin = customerService.findCustomerByEmail(customer.getEmail());
        if (customerCheckLogin != null && customerCheckLogin.getPassword().equals(customer.getPassword())) {
            if (customerCheckLogin.isEnabled()) {
                return ResponseEntity.ok().body(customerCheckLogin);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Account has not been activated");
            }
        } else {
            return new ResponseEntity<>("Wrong Account Or Password", HttpStatus.BAD_REQUEST);
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


    // sửa thông tin customer
    @PutMapping("update/{customerId}")
    public ResponseEntity<?> updateCustomer(@PathVariable Integer customerId, @RequestBody Customer customer) {
        Optional<Customer> customer1 = customerService.getCustomerById(customerId);
        if (customer1.isPresent()) {
            Customer customer2 = customer1.get();
            customer2.setAddressLine1(customer.getAddressLine1());
            customer2.setAddressLine2(customer.getAddressLine2());
            customer2.setCity(customer.getCity());
            customer2.setFirstName(customer.getFirstName());
            customer2.setLastName(customer.getLastName());
            customer2.setPhoneNumber(customer.getPhoneNumber());
            customer2.setState(customer.getState());
            customer2.setEmail(customer.getEmail());
            customer2.setCountry(customer.getCountry());
            customerService.saveCustomer(customer2);
            return new ResponseEntity<>(customer2,HttpStatus.OK);

        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // đổi mật khẩu customer
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
