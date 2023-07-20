package com.team3.ecommerce.service;

import com.team3.ecommerce.entity.User;
import com.team3.ecommerce.repository.AccountUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AccountUserService {
    @Autowired
    private AccountUserRepository accountUserRepository;

    // hiển thị toàn bộ tài khoản User
    public Page<User> listUsers(Pageable pageable){
        return accountUserRepository.findAll(pageable);

    }
}
