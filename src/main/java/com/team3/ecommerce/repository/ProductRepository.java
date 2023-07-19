package com.team3.ecommerce.repository;

import com.team3.ecommerce.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;


@Repository
@Transactional
public interface ProductRepository extends JpaRepository<Product,Integer> {
}
