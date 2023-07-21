package com.team3.ecommerce.service;

import com.team3.ecommerce.entity.Brand;
import com.team3.ecommerce.entity.Category;
import com.team3.ecommerce.entity.Shop;
import com.team3.ecommerce.entity.Shop;
import com.team3.ecommerce.entity.product.Product;
import com.team3.ecommerce.repository.CategoryRepository;
import com.team3.ecommerce.repository.IShopRepository;
import com.team3.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.awt.*;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductService{
    @Autowired
    private ProductRepository iProductRepository;

    public Iterable<Product> findAll() {
        return iProductRepository.findAll();
    }

    public Optional<Product> findById(Integer id) {
        Product list = iProductRepository.findById(id).get();
        return iProductRepository.findById(id);
    }
    public Product save(Product product) {
        return iProductRepository.save(product);
    }

    public void remove(Integer id) {
        iProductRepository.deleteById(id);
    }

    // tìm kiếm sản phẩm theo id của 1 shop
    public Optional<Product> findProductsInShopByIdProducts(Integer idProducts, Shop shop){

            return iProductRepository.findProductsInShopByIdProducts(idProducts,shop);

    }

    //Toàn bộ sản phẩm của 1 shop: theo các tiêu chí
    public Page<Product> findByShop(Shop shop, String keyword, Category category, Brand brand, Pageable pageable) {
        if (shop !=null && shop.isEnabled()) {
            return iProductRepository.findProductByShopKeywordAndCategory(shop, keyword, category, brand, pageable);
        } else {
            return null;
        }
    }

    // hiển thị 5 sản phẩm bán chạy nhất
    public Iterable<Product> showTopProductsRating(){
        return iProductRepository.showTopProductsRating();
    }
}
