package com.team3.ecommerce.repository;

import com.team3.ecommerce.entity.Customer;
import com.team3.ecommerce.entity.Shop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface IShopRepository extends JpaRepository<Shop, Integer> {
    //    Shop save(Shop shop, Customer customer);
    // Tìm kiếm theo name shop.
    Iterable<Shop> findAllByNameContaining(String name);

    @Query("SELECT s FROM Shop s WHERE s.customer = :customer")
    Page<Shop> findShopByCustomer(@Param("customer") Customer customer, Pageable pageable);

    Iterable<Shop> findShopByCustomer(Customer customer);

    @Query("UPDATE Shop s SET s.enabled = ?2 WHERE s.id = ?1")
    @Modifying
    public void updateEnabledStatus(Integer id, boolean enabled);
}
