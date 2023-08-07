package com.team3.ecommerce.repository;

import com.team3.ecommerce.entity.Shop;
import com.team3.ecommerce.entity.Brand;
import com.team3.ecommerce.entity.Category;
import com.team3.ecommerce.entity.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import javax.transaction.Transactional;


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


    Iterable<Product> findTop6ByOrderByDiscountPercentDesc();

    // hiển thị danh sách product của customer
    Page<Product> findByCustomerId(Integer customerId, Pageable pageable);
    List<Product> findByCustomerId(Integer customerId);

    // hiển thị tất cả ảnh
    @Query("SELECT p.mainImage, pi.name as imageName FROM Product p LEFT JOIN p.images pi WHERE p.id = :productId")
    List<Object[]> findProductImages(@Param("productId") Integer productId);

    Page<Product> findByCategory(Category category, Pageable pageable);

    List<Product> findByCategory(Category category);

    Iterable<Product> findTop10ByOrderByDiscountPercentDesc();


    // tìm kiếm theo tên
    @Query("SELECT p FROM  Product p WHERE p.name LIKE %:name% or p.alias LIKE %:name%" )
    Iterable<Product> findByNameProduct(@Param("name") String name);

    // lấy 3 sản pẩm mới nhất
    Iterable<Product> findTop3ByOrderByIdDesc();

    // lấy 3 sản phẩm mới nhất của danh mục
        List<Product> findTop3ByCategoryOrderByCreatedTimeDesc(Category category);




}
