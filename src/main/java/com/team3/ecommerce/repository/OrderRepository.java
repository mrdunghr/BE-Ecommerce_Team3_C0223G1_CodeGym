package com.team3.ecommerce.repository;

import com.team3.ecommerce.entity.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query(value = "select * from orders where customer_id = ?1", nativeQuery = true)
    List<Order> findAllByCustomer(Integer customerId);

    @Query(value = "select * from orders where customer_id = ?1 ORDER BY order_time DESC limit 1", nativeQuery = true)
    Order findLatestOrderByCustomerId(Integer customerId);
}
