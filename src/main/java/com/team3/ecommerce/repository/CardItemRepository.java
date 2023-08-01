package com.team3.ecommerce.repository;

import com.team3.ecommerce.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardItemRepository extends JpaRepository<CartItem, Integer> {
    List<CartItem> findByCustomerId(Integer id);
}
