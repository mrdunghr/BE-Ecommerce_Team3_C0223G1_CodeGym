package com.team3.ecommerce.repository;

import com.team3.ecommerce.entity.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}
