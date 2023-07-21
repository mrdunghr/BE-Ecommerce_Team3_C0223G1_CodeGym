package com.team3.ecommerce.controller;

import com.team3.ecommerce.entity.Category;
import com.team3.ecommerce.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;
    // Lấy ra list category
    @GetMapping("")
    public ResponseEntity<Iterable<Category>> showAllCategory() {
        Iterable<Category> categoryIterable = categoryService.findAllCategory();
        return ResponseEntity.ok(categoryIterable);
    }
}
