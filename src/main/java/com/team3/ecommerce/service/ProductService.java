package com.team3.ecommerce.service;

import com.team3.ecommerce.entity.Brand;
import com.team3.ecommerce.entity.Category;
import com.team3.ecommerce.entity.Customer;
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
import java.time.Instant;
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

    public Product editProduct(Product product) {
        return iProductRepository.save(product);
    }

    public Product createProduct(Product product) {
        // Kiểm tra xem sản phẩm có cùng tên đã tồn tại hay chưa
        boolean existsWithNameOrAlias = iProductRepository.existsByNameOrAlias(product.getName(), product.getAlias());
        if (existsWithNameOrAlias) {
            throw new IllegalArgumentException("Product name or Alias must be unique");
        }
        product.setEnabled(true);
        product.setCreatedTime(Date.from(Instant.now()));
        product.setInStock(true);
        product.setAverageRating(0);
//        product.setMainImage("");
        product.setReviewCount(0);

        // Nếu danh sách ảnh không rỗng, lưu danh sách ảnh vào cơ sở dữ liệu và gán sản phẩm cho mỗi ảnh
        if (!product.getImages().isEmpty()) {
            Set<ProductImage> images = product.getImages();
            for (ProductImage image : images) {
                image.setProduct(product);
                // lưu ảnh vào cơ sở dữ liệu
                productImageRepository.save(image);
            }
        }
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

    // hiển thị 6 sản phẩm bán chạy nhất
    public Iterable<Product> findTop6ByOrderByDiscountPercent() {
        return iProductRepository.findTop6ByOrderByDiscountPercentDesc();
    }

    // hiển thị 10 sản phẩm bán chạy nhất
    public Iterable<Product> findTop10ByOrderByDiscountPercent() {
        return iProductRepository.findTop10ByOrderByDiscountPercentDesc();
    }

    // hiển thị danh sách product của customer
    public Page<Product> getAllProductsByCustomerId(Integer customerId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return iProductRepository.findByCustomerId(customerId, pageable);
    }

    // hiển thị tất cả ảnh của sản phẩm
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

    //sửa ảnh sản phẩm
    @Transactional
    public Product updateProductImages(Integer productId, Map<String, Object> imageInfo) {
        String mainImage = (String) imageInfo.get("mainImage");
        List<String> images = (List<String>) imageInfo.get("images");
        Product product = iProductRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id " + productId));
        // Cập nhật ảnh chính của sản phẩm
        product.setMainImage(mainImage);
        iProductRepository.save(product);
        // Xóa tất cả các ảnh phụ của sản phẩm trong database
        productImageRepository.deleteByProduct(product);
        // Thêm các ảnh mới vào sản phẩm
        List<ProductImage> productImages = new ArrayList<>();
        for (String imageName : images) {
            ProductImage productImage = new ProductImage();
            productImage.setProduct(product);
            productImage.setName(imageName);
            productImages.add(productImage);
        }
        productImageRepository.saveAll(productImages);
        return product;
    }

    // hiển thị product theo category phân trang
    public Page<Product> getProductsByCategory(Category category, Pageable pageable) {
        return iProductRepository.findByCategory(category, pageable);
    }

    // hiển thị product theo category toàn bộ
    public List<Product> getAllProductByCategory(Category category) {
        return iProductRepository.findByCategory(category);
    }

    // tìm kiếm theo tên
    public Iterable<Product> findByNameProduct(String name) {
        return iProductRepository.findByNameProduct(name);
    }

    // lấy 3 sản phẩm mới nhất
    public Iterable<Product> findTop3ByOrderByIdDesc() {
        return iProductRepository.findTop3ByOrderByIdDesc();
    }

    public boolean productIsExisted(Integer id) {
        return iProductRepository.existsById(id);
    }

    public List<Product> showProductByCustomer(Integer customerId) {
        return iProductRepository.findByCustomerId(customerId);
    }

    // lâ 3 sản phẩm mới nhất của danh mục
    public List<Product> getLatestProductsByCategory(Category category) {
        return iProductRepository.findTop3ByCategoryOrderByCreatedTimeDesc(category);
    }

}
