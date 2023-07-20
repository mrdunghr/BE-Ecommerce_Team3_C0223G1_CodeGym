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

    public Optional<Category> findCategoryById(Integer id){
        return categoryRepository.findById(id);
    }
}
