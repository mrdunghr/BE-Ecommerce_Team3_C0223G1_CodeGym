package com.team3.ecommerce.repository;

import com.team3.ecommerce.entity.product.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImagRepository extends JpaRepository<ProductImage, Integer> {
}
