package com.team3.ecommerce.repository;

import com.team3.ecommerce.entity.product.Product;
import com.team3.ecommerce.entity.product.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage,Integer> {
    void deleteByProduct(Product product);
}
