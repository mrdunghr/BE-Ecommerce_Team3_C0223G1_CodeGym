package com.team3.ecommerce.repository;

import com.team3.ecommerce.entity.order.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderDetailsRepository extends JpaRepository<OrderDetail, Integer> {

    @Query("SELECT od FROM OrderDetail od JOIN od.product p JOIN p.shop s WHERE s.id = ?1")
    List<OrderDetail> findOrderDetailByShopId(Integer shopId);
}
