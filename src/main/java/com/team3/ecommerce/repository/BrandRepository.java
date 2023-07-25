package com.team3.ecommerce.repository;

import com.team3.ecommerce.entity.Brand;
import com.team3.ecommerce.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Integer> {
    // tìm brand theo 1 category
    @Query("SELECT b FROM Brand b WHERE :category MEMBER OF b.categories")
    Iterable<Brand> findBrandByCategory(@Param("category") Category category);
}
