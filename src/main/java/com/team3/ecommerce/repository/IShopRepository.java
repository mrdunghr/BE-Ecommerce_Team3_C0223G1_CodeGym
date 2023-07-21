package com.team3.ecommerce.repository;

import com.team3.ecommerce.entity.Customer;
import com.team3.ecommerce.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface IShopRepository extends JpaRepository<Shop, Integer> {
    //    Shop save(Shop shop, Customer customer);
    // Tìm kiếm theo name shop.
    Iterable<Shop> findAllByNameContaining(String name);
}
