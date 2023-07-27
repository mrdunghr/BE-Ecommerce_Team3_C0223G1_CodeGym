package com.team3.ecommerce.service;

import com.team3.ecommerce.entity.Category;
import com.team3.ecommerce.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    // kiểm tra xem category có trong hệ thống hay không
    public boolean checkCategoryExists(Integer categoryId) {
        return categoryRepository.existsById(categoryId);
    }
    public Optional<Category> findCategoryById(Integer id) {
        return categoryRepository.findById(id);
    }

    public Iterable<Category> findAllCategory() {
        return categoryRepository.findAll();
    }

    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }
    public void updateCategoryEnabledStatus(Integer id, boolean enabled){
        categoryRepository.updateEnabledStatus(id, enabled);
    }
    public void deleteCategoryById(Integer id) {
        categoryRepository.deleteById(id);
    }
}
