package com.team3.ecommerce.repository;

import com.team3.ecommerce.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface AccountCustomerRepository extends JpaRepository<Customer,Integer> {
}
