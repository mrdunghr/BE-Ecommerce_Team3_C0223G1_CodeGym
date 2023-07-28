package com.team3.ecommerce.controller;

import com.team3.ecommerce.entity.Category;
import com.team3.ecommerce.entity.User;
import com.team3.ecommerce.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    // Lấy ra list category
    @GetMapping("/all")
    public ResponseEntity<Iterable<Category>> showAllCategory() {
        Iterable<Category> categoryIterable = categoryService.findAllCategory();
        return ResponseEntity.ok(categoryIterable);
    }

    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @PostMapping("/create-category")
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        Category newCategory = categoryService.saveCategory(category);
        return ResponseEntity.ok(newCategory);
    }

    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @PutMapping("/{id}/enabled/{status}")
    public ResponseEntity<String> updateUserEnabledStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean enabled) {
        try {
            categoryService.updateCategoryEnabledStatus(id, enabled);
            String status = enabled ? "enabled" : "disabled";
            String message = "The Category ID " + id + " has been " + status;
            return ResponseEntity.ok(message);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        Category category = categoryService.findCategoryById(id).get();
        if (category == null) {
            return ResponseEntity.notFound().build();
        }
        categoryService.deleteCategoryById(id);
        return ResponseEntity.ok("Category deleted successfully.");
    }
}
