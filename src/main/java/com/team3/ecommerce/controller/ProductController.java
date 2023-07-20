package com.team3.ecommerce.controller;

import com.team3.ecommerce.entity.Brand;
import com.team3.ecommerce.entity.Category;
import com.team3.ecommerce.entity.Shop;
import com.team3.ecommerce.entity.product.Product;
import com.team3.ecommerce.service.BrandService;
import com.team3.ecommerce.service.CategoryService;
import com.team3.ecommerce.service.ProductService;
import com.team3.ecommerce.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.relational.core.sql.In;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private ShopService shopService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    BrandService brandService;

    @PostMapping("/add")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        return new ResponseEntity<>(productService.save(product), HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<Iterable<Product>> allProduct() {
        return new ResponseEntity<>(productService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<Product> detailProduct(@PathVariable Integer id) {
        Optional<Product> productOptional = productService.findById(id);
        if (!productOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(productOptional.get(), HttpStatus.OK);
    }


    // Hiển thị danh sách sản phẩm trong 1 shop theo các tiêu chí: tìm theo tên, tìm theo category, tìm theo brand
    @GetMapping("/shop/{idShop}")
    public ResponseEntity<Iterable<Product>> findProductByShop(@PathVariable Integer idShop,
                                                               @RequestParam(value = "keyword", required = false) String keyword,
                                                               @RequestParam(value = "idCategory", required = false) Integer idCategory,
                                                               @RequestParam(value = "idBrand", required = false) Integer idBrand,
                                                               @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Optional<Shop> shop = shopService.findByIdShop(idShop);
        if (shop != null && shop.isPresent()) {
            if (idCategory != null) {
                Category category = categoryService.findCategoryById(idCategory).get();
                if (idBrand != null) {
                    Brand brand = brandService.findBrandById(idBrand).get();
                    Pageable pageable = PageRequest.of(page, size);
                    Page<Product> products = productService.findByShop(shop.get(), keyword, category, brand, pageable);
                    return new ResponseEntity<>(products, HttpStatus.OK);
                } else {
                    Pageable pageable = PageRequest.of(page, size);
                    Page<Product> products = productService.findByShop(shop.get(), keyword, category, null, pageable);
                    return new ResponseEntity<>(products, HttpStatus.OK);
                }
            } else {
                if (idBrand != null) {
                    Brand brand = brandService.findBrandById(idBrand).get();
                    Pageable pageable = PageRequest.of(page, size);
                    Page<Product> products = productService.findByShop(shop.get(), keyword, null, brand, pageable);
                    return new ResponseEntity<>(products, HttpStatus.OK);
                } else {
                    Pageable pageable = PageRequest.of(page, size);
                    Page<Product> products = productService.findByShop(shop.get(), keyword, null, null, pageable);
                    return new ResponseEntity<>(products, HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
