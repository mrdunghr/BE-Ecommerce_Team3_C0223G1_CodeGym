package com.team3.ecommerce.controller;

import com.team3.ecommerce.entity.Country;
import com.team3.ecommerce.entity.Customer;
import com.team3.ecommerce.jwt.JwtTokenProvider;
import com.team3.ecommerce.payload.req.LoginReq;
import com.team3.ecommerce.payload.req.SignupReq;
import com.team3.ecommerce.payload.resp.JwtResp;
import com.team3.ecommerce.payload.resp.MessageResp;
import com.team3.ecommerce.security.CustomerDetails;
import com.team3.ecommerce.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/customers")
@CrossOrigin("*")
public class CustomerController {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private AuthenticationManager authenticationManager; // giúp xác thực
    @Autowired
    private JwtTokenProvider tokenProvider;  //lâ userName từ token
    @Autowired
    private PasswordEncoder passwordEncoder; // để mã hóa password
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Integer id) {
        Optional<Customer> customer = customerService.getCustomerById(id);
        if (customer.isPresent()) {
            return ResponseEntity.ok(customer.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @GetMapping("/check-email")
    public ResponseEntity<Boolean> isEmailUnique(@RequestParam String email) {
        boolean isUnique = customerService.isEmailUnique(email);
        return ResponseEntity.ok(isUnique);
    }

    @PostMapping("/register")
//    public ResponseEntity<String> registerCustomer(@RequestBody Customer customer) {
//        boolean isUnique = customerService.isEmailUnique(customer.getEmail());
//        if (!isUnique) {
//            return ResponseEntity.badRequest().body("Email already exists.");
//        }
//
//        customerService.registerCustomer(customer);
//        return ResponseEntity.ok("Registration successful.");
//    }
    public ResponseEntity<?> registerUser(@RequestBody SignupReq signupReq){
        if(customerService.existsByEmail(signupReq.getEmail())){
            return ResponseEntity.badRequest().body(new MessageResp("Error: Email is already !!!"));
        }
        Customer customer = new Customer();
        customer.setFirstName(signupReq.getFirstName());
        customer.setLastName(signupReq.getLastName());
        customer.setEmail(signupReq.getEmail());
        customer.setPassword(passwordEncoder.encode(signupReq.getPassword()));
        customer.setPhoneNumber(signupReq.getPhoneNumber());
        customer.setEnabled(true);
        customer.setState(signupReq.getState());
        customer.setCountry(signupReq.getCountry());
        customer.setCity(" ");
        customer.setPostalCode(" ");
        customer.setAddressLine1(" ");
        customer.setAddressLine2(" ");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy ");
        Date dateNow = new Date();
        String strNow = sdf.format(dateNow);
        try{
            customer.setCreatedTime(sdf.parse(strNow));
        }catch (Exception e){
            e.printStackTrace();
        }

        customerService.saveCustomer(customer);
        return ResponseEntity.ok(new MessageResp("User registered successfully"));
    }

    @PostMapping("/login")
//    public ResponseEntity<?> LoginCustomer(@RequestBody Customer customer) {
//        Customer customer1 = customerService.findCustomerByEmail(customer.getEmail());
//        if (customer1 != null && customer1.getPassword().equals(customer.getPassword())) {
//            return new ResponseEntity<>(customer1, HttpStatus.ACCEPTED);
//        } else {
//            return ResponseEntity.badRequest().body("Wrong Email or Password");
//        }
//    }
    public ResponseEntity<?> loginUser(@RequestBody LoginReq loginReq){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginReq.getEmail(),loginReq.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomerDetails customerDetails = (CustomerDetails) authentication.getPrincipal();

        // sinh ra JWT trả về cho client
        String jwt = tokenProvider.generateToken(customerDetails);
        return  ResponseEntity.ok(new JwtResp(jwt,customerDetails.getEmail()));
    }

    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @GetMapping("/list-country")
    public ResponseEntity<List<Country>> getCountryList() {
        return new ResponseEntity<>(customerService.listAllCountries(), HttpStatus.OK);
    }

}
