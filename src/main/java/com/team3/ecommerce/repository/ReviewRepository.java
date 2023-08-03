package com.team3.ecommerce.repository;

import com.team3.ecommerce.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Integer> {
    Iterable<Review> findAllByProduct_Id(Integer productId);


}
