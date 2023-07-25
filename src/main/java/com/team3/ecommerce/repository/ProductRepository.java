package com.team3.ecommerce.repository;

import com.team3.ecommerce.entity.Shop;
import com.team3.ecommerce.entity.Brand;
import com.team3.ecommerce.entity.Category;
import com.team3.ecommerce.entity.Shop;
import com.team3.ecommerce.entity.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import javax.transaction.Transactional;
import java.util.Optional;


@Repository
@Transactional
public interface ProductRepository extends JpaRepository<Product,Integer> {

    // check tên sản phẩm
    boolean existsByNameOrAlias(String name, String alias);


//    @Query("SELECT p FROM Product p WHERE p.name LIKE %:keyword% OR p.alias LIKE%:keyword%")
    @Query("SELECT p FROM Product p WHERE p.shop = :shop AND" +
            "(:keyword IS NULL OR (p.name LIKE %:keyword% OR p.alias LIKE %:keyword%)) AND " +
            "(:category IS NULL OR p.category = :category) AND " +
            "(:brand IS NULL OR p.brand = :brand)")
    Page<Product> findProductByShopKeywordAndCategory(@Param("shop") Shop shop,
                                                          @Param("keyword") String keyword,
                                                          @Param("category") Category category,
                                                          @Param("brand")Brand brand,Pageable pageable);


    Iterable<Product> findTop5ByOrderByDiscountPercentDesc();

    // hiển thị danh sách product của customer
    Page<Product> findByCustomerId(Integer customerId, Pageable pageable);
}
