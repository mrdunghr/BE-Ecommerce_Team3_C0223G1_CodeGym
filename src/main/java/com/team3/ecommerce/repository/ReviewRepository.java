package com.team3.ecommerce.repository;

import com.team3.ecommerce.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Integer> {
    Iterable<Review> findAllByProduct_Id(Integer productId);

    @Query("SELECT COUNT(r.id) FROM Review r WHERE r.customer.id = ?1 AND r.product.id = ?2")
    public Long countByCustomerAndProduct(Integer customerId, Integer productId);
}
