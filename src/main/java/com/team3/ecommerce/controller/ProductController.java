package com.team3.ecommerce.controller;

import com.team3.ecommerce.entity.product.Product;
import com.team3.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
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

    // Thêm mới 1 sản phẩm
    @PostMapping("/add")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        return new ResponseEntity<>(productService.save(product), HttpStatus.CREATED);
    }

    // Hiển thị danh sách sản phẩm
    @GetMapping("/all")
    public ResponseEntity<Iterable<Product>> allProduct() {
        return new ResponseEntity<>(productService.findAll(), HttpStatus.OK);
    }

    // Chi tiêết sản phẩm
    @GetMapping("/detail/{id}")
    public ResponseEntity<Product> detailProduct(@PathVariable Integer id){
        Optional<Product> productOptional = productService.findById(id);
        if (!productOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(productOptional.get(), HttpStatus.OK);
    }

    // Sửa sản phẩm
    @PutMapping("/edit/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Integer id, @RequestBody Product products) {
        if (this.productService.findById(id).isPresent()) {
            products.setId(id);
            this.productService.save(products);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
