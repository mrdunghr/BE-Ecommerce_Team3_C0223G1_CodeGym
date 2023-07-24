package com.team3.ecommerce.service;

import com.team3.ecommerce.entity.Brand;
import com.team3.ecommerce.repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class BrandService {
    @Autowired
    BrandRepository brandRepository;

    public Optional<Brand> findBrandById(Integer id) {
        return brandRepository.findById(id);
    }
}
