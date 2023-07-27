package com.team3.ecommerce.service;

import com.team3.ecommerce.entity.User;
import com.team3.ecommerce.repository.AccountUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        return accountUserRepository.save(user);
    }
    public User findUserById(Integer id) {
        return accountUserRepository.findById(id).get();
    }
    public void deleteUserById(Integer id) {
        accountUserRepository.deleteById(id);
    }

    public void updateUserEnabledStatus(Integer id, boolean enabled){
        accountUserRepository.updateEnabledStatus(id, enabled);
    }
}
