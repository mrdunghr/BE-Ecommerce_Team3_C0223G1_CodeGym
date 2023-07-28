package com.team3.ecommerce.service;

import com.team3.ecommerce.entity.AuthenticationType;
import com.team3.ecommerce.entity.Country;
import com.team3.ecommerce.entity.Customer;
import com.team3.ecommerce.repository.CountryRepository;
import com.team3.ecommerce.repository.CustomerRepository;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CustomerService {
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private CustomerRepository customerRepository;

    public Optional<Customer> getCustomerById(Integer id) {
        return customerRepository.findById(id);
    }// lấy ra một customer

    public List<Country> listAllCountries() {
        return countryRepository.findAllByOrderByNameAsc();
    } // danh sách quốc gia

    public boolean isEmailUnique(String email) {
        Customer customer = customerRepository.findByEmail(email);
        return customer == null;
    } // kiểm tra email là duy nhất

    public void registerCustomer(Customer customer) {
        customer.setEnabled(true);
        customer.setCreatedTime(new Date());
        customer.setAuthenticationType(AuthenticationType.DATABASE);
        customer.setAddressLine1(" ");
        customer.setAddressLine2(" ");
        customer.setCity(" ");
        customer.setPostalCode(" ");

        String randomCode = RandomString.make(64);
        customer.setVerificationCode(randomCode);

        customerRepository.save(customer);
    }

    public Customer findCustomerByEmail(String email) {
        return customerRepository.findByEmail(email);
    }
    // đăng kí tài khoản user customer mới

    // tìm kiếm Customer theo id
    public Optional<Customer> findById(Integer id) {
        return customerRepository.findById(id);
    }

    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public void updateUserEnabledStatus(Integer id, boolean enabled){
        customerRepository.updateEnabledStatus(id, enabled);
    }
}
