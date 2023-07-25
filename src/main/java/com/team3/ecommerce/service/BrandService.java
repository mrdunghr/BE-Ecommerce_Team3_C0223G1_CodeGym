package com.team3.ecommerce.service;

import com.team3.ecommerce.entity.Brand;
import com.team3.ecommerce.entity.Category;
import com.team3.ecommerce.repository.BrandRepository;
import com.team3.ecommerce.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class BrandService {
    @Autowired
    BrandRepository brandRepository;
    @Autowired
    CategoryRepository categoryRepository;

    public Optional<Brand> findBrandById(Integer id) {
        return brandRepository.findById(id);
    }

    public Iterable<Brand> findBrandByCategory(Integer id){
        Category category = categoryRepository.findById(id).get();
        if (category != null) {
            return brandRepository.findBrandByCategory(category);
        } else {
            throw new IllegalArgumentException("Not found or does not exist");
        }
    }
}
