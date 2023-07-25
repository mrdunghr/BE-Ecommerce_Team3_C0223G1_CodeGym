package com.team3.ecommerce.service;

import com.team3.ecommerce.entity.User;
import com.team3.ecommerce.repository.AccountUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountUserService {
    @Autowired
    private AccountUserRepository accountUserRepository;

    // hiển thị toàn bộ tài khoản User
    public Page<User> listUsers(Pageable pageable) {
        return accountUserRepository.findAll(pageable);
    }

    public User findUserByEmail(String email) {
        return accountUserRepository.findByEmail(email);
    }

    public List<User> listAll(){
        return accountUserRepository.findAll();
    }
    public User createUser(User user) {
        user.setEnabled(true);
        user.setPassword("123456");
        user.setPhotos(null);
        return accountUserRepository.save(user);
    }
}
