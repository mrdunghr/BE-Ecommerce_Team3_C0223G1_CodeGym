package com.team3.ecommerce.repository;

import com.team3.ecommerce.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImageRepository,Integer> {
    void deleteByProduct(Product product);
}
