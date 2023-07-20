package com.team3.ecommerce.repository;

import com.team3.ecommerce.entity.Shop;
import com.team3.ecommerce.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;


@Repository
@Transactional
public interface ProductRepository extends JpaRepository<Product,Integer> {
    // tìm kiếm sản phẩm của 1 shop
    @Query("SELECT p FROM Product p WHERE p.id = :idProducts AND p.shop = :Shop")
    Optional<Product> findProductsInShopByIdProducts(@Param("idProducts") Integer idProducts, @Param("Shop") Shop shop);
}
