package com.team3.ecommerce.controller;

import com.team3.ecommerce.entity.Brand;
import com.team3.ecommerce.entity.Category;
import com.team3.ecommerce.service.BrandService;
import com.team3.ecommerce.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@CrossOrigin
@RestController
@RequestMapping("/api/v1/brand")
public class BrandController {

    @Autowired
    BrandService brandService;
    @Autowired
    CategoryService categoryService;

    @GetMapping("/category-brand")
    public ResponseEntity<?> searchBrandByCategories(@RequestParam("categoryIds") Integer categoryIds) {
        Optional<Category> optionalCategory = categoryService.findCategoryById(categoryIds);
        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            Iterable<Brand> brands = brandService.findBrandByCategory(category.getId());
            if (brands.iterator().hasNext()) {
                return ResponseEntity.ok(brands);
            } else {
                return ResponseEntity.badRequest().body("No brands found for the given category.");
            }
        } else {
            return ResponseEntity.badRequest().body("Category with the given ID not found.");
        }
    }

}
