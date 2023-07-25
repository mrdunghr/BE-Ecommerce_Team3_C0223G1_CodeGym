package com.team3.ecommerce.service;

import com.team3.ecommerce.entity.Brand;
import com.team3.ecommerce.entity.Category;
import com.team3.ecommerce.entity.Shop;
import com.team3.ecommerce.entity.product.Product;
import com.team3.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Service
@Transactional
public class ProductService {
    @Autowired
    private ProductRepository iProductRepository;

    public Iterable<Product> findAll() {
        return iProductRepository.findAll();
    }

    public Optional<Product> findById(Integer id) {
        Product list = iProductRepository.findById(id).get();
        return iProductRepository.findById(id);
    }

    public Product editProduct(Product product) {
        return iProductRepository.save(product);
    }

    public Product save(Product product) {
        // Kiểm tra xem sản phẩm có cùng tên đã tồn tại hay chưa
        boolean existsWithNameOrAlias = iProductRepository.existsByNameOrAlias(product.getName(), product.getAlias());
        if (existsWithNameOrAlias) {
            throw new IllegalArgumentException("Product name or Alias must be unique");
        }
        product.setEnabled(true);
        product.setCreatedTime(Date.from(Instant.now()));
        product.setInStock(true);
        product.setAverageRating(0);
        product.setDiscountPercent(0);
        product.setMainImage("");
        product.setReviewCount(0);
        return iProductRepository.save(product);
    }

    public void remove(Integer id) {
        iProductRepository.deleteById(id);
    }


    //Toàn bộ sản phẩm của 1 shop: theo các tiêu chí
    public Page<Product> findByShop(Shop shop, String keyword, Category category, Brand brand, Pageable pageable) {
        if (shop != null && shop.isEnabled()) {
            return iProductRepository.findProductByShopKeywordAndCategory(shop, keyword, category, brand, pageable);
        } else {
            return null;
        }
    }

    // hiển thị 5 sản phẩm bán chạy nhất
    public Iterable<Product> findTop5ByOrderByDiscountPercent() {
        return iProductRepository.findTop5ByOrderByDiscountPercentDesc();
    }

    // hiển thị danh sách product của customer
    public Page<Product> getAllProductsByCustomerId(Integer customerId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return iProductRepository.findByCustomerId(customerId, pageable);
    }


}
