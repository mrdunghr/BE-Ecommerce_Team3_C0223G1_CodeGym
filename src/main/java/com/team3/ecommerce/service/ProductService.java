package com.team3.ecommerce.service;

import com.team3.ecommerce.entity.product.Product;
import com.team3.ecommerce.repository.IShopRepository;
import com.team3.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductService{
    @Autowired
    private ProductRepository iProductRepository;

    public Iterable<Product> findAll() {
        return iProductRepository.findAll();
    }

    public Optional<Product> findById(Integer id) {
        Product list = iProductRepository.findById(id).get();
        return iProductRepository.findById(id);
    }
    public Product save(Product product) {
        return iProductRepository.save(product);
    }

    public void remove(Integer id) {
        iProductRepository.deleteById(id);
    }
}
