package com.team3.ecommerce.service;

import com.team3.ecommerce.entity.Customer;
import com.team3.ecommerce.repository.AccountCustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AccountCustomerService {
    @Autowired
    private AccountCustomerRepository accountCustomerRepository;

    public Page<Customer> listCustomer(Pageable pageable){
        return accountCustomerRepository.findAll(pageable);
    }


}
