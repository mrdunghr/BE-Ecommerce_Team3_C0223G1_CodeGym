package com.team3.ecommerce.service;

import com.team3.ecommerce.entity.Brand;
import com.team3.ecommerce.entity.Category;
import com.team3.ecommerce.entity.Shop;
import com.team3.ecommerce.entity.product.Product;
import com.team3.ecommerce.entity.product.ProductImage;
import com.team3.ecommerce.repository.ProductImageRepository;
import com.team3.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class ProductService {
    @Autowired
    private ProductRepository iProductRepository;
    @Autowired
    private ProductImageRepository productImageRepository;

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
    public Optional<Product> findProductsInShopByIdProducts(Integer idProducts, Shop shop) {

        return iProductRepository.findProductsInShopByIdProducts(idProducts, shop);

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

    // hiển thị tất cả ảnh
    public Map<String, Object> getProductWithImages(Integer productId) {
        List<Object[]> results = iProductRepository.findProductImages(productId);
        Map<String, Object> productWithImages = new HashMap<>();
        for (Object[] result : results) {
            String mainImage = (String) result[0];
            String imageName = (String) result[1];
            if (!productWithImages.containsKey("mainImage")) {
                productWithImages.put("mainImage", mainImage);
            }
            List<String> images = (List<String>) productWithImages.getOrDefault("images", new ArrayList<>());
            images.add(imageName);
            productWithImages.put("images", images);
        }
        return productWithImages;
    }

    @Transactional
    public Product updateProductImages(Integer productId, Map<String, Object> imageInfo) {
        String mainImage = (String) imageInfo.get("mainImage");
        List<String> images = (List<String>) imageInfo.get("images");
        Product product = iProductRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id " + productId));
        product.setMainImage(mainImage);
        iProductRepository.save(product);
        // Xóa tất cả các ảnh phụ của sản phẩm trong database
        productImageRepository.deleteByProduct(product);
        List<ProductImage> productImages = new ArrayList<>();  // Thêm các ảnh mới vào sản phẩm
        for (String imageName : images) {
            ProductImage productImage = new ProductImage();
            productImage.setProduct(product);
            productImage.setName(imageName);
            productImages.add(productImage);
        }
       // productImageRepository.saveAll(productImages);
        return product;
    }
}
