package com.team3.ecommerce.controller;

import com.team3.ecommerce.entity.ChangePasswordRequest;
import com.team3.ecommerce.entity.Country;
import com.team3.ecommerce.entity.Customer;
import com.team3.ecommerce.jwt.JwtTokenProvider;
import com.team3.ecommerce.payload.req.LoginReq;
import com.team3.ecommerce.payload.req.SignupReq;
import com.team3.ecommerce.payload.resp.JwtResp;
import com.team3.ecommerce.payload.resp.MessageResp;
import com.team3.ecommerce.security.CustomerDetails;
import com.team3.ecommerce.entity.User;
import com.team3.ecommerce.security.CustomerDetailsService;
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

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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
        if (Objects.isNull(existingCustomer)) {
            return ResponseEntity.notFound().build();
        }
        customerService.deleteCustomerById(id);
        return ResponseEntity.ok("Customer deleted successfully.");
    }

    // đổi mật khẩu
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest, HttpServletRequest request){
        try {
            // Lấy chuỗi JWT từ header "Authorization" của request
            String bearerToken = request.getHeader("Authorization");
            String jwt = tokenProvider.resolveToken(bearerToken);
            // Lấy email của người dùng từ chuỗi JWT
            String email = tokenProvider.getCustomerFromJwt(jwt);
            // Xác thực người dùng và lấy thông tin chi tiết của người dùng
            Customer customer = customerService.findCustomerByEmail(email);
            // Xác minh mật khẩu cũ của người dùng
            if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(), customer.getPassword())) {
                return new ResponseEntity<>("Old password is incorrect", HttpStatus.BAD_REQUEST);
            }
            // Đặt mật khẩu mới cho người dùng
            String newPassword = passwordEncoder.encode(changePasswordRequest.getNewPassword());
            customer.setPassword(newPassword);
            customerService.saveCustomer(customer);
            return new ResponseEntity<>("Password was successfully changed", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error while changing password", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
