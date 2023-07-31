package com.team3.ecommerce.controller;

import com.team3.ecommerce.entity.Brand;
import com.team3.ecommerce.entity.Category;
import com.team3.ecommerce.entity.User;
import com.team3.ecommerce.service.BrandService;
import com.team3.ecommerce.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/all")
    public ResponseEntity<?> showAllBrand( @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "5") int size,
                                           @RequestParam(defaultValue = "false") boolean list) {
        if (list) {
            Iterable<Brand> brands = brandService.showBrandList();
            return ResponseEntity.ok(brands);
        } else {
            Pageable pageable = PageRequest.of(page, size);
            Page<Brand> brands = brandService.showBrandPage(pageable);
            return ResponseEntity.ok(brands);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Brand> getBrandById(@PathVariable Integer id) {
        return ResponseEntity.ok(brandService.findBrandById(id).get());
    }

    @PostMapping("/create-brand")
    public ResponseEntity<Brand> saveBrand(@RequestBody Brand brand) {
        return ResponseEntity.ok(brandService.save(brand));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        Brand existingBrand = brandService.findBrandById(id).get();
        if (existingBrand == null) {
            return ResponseEntity.notFound().build();
        }
        brandService.deleteBrandsById(id);
        return ResponseEntity.ok("Brand deleted successfully.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Brand> updateBrand(@PathVariable Integer id, @RequestBody Brand updateBrand) {
        Brand existingBrand = brandService.findBrandById(id).get();
        if (existingBrand == null) {
            return ResponseEntity.notFound().build();
        }
        existingBrand.setName(updateBrand.getName());
        existingBrand.setLogo(updateBrand.getLogo());
        existingBrand.setCategories(updateBrand.getCategories());

        updateBrand = brandService.save(existingBrand);
        return new ResponseEntity<>(updateBrand, HttpStatus.OK);
    }
}
