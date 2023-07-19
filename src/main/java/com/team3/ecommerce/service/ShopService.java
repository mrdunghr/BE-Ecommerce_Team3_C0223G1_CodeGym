package com.team3.ecommerce.service;

import com.team3.ecommerce.entity.Customer;
import com.team3.ecommerce.entity.Shop;
import com.team3.ecommerce.repository.IShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class ShopService {

    @Autowired
    private IShopRepository shopRepository;




    public Iterable<Shop> findAllShop(){
        return shopRepository.findAll();
    }


    public Shop createShop(Shop shop, Customer customer) {

        shop.setCreatedTime(new Date());
        shop.setEnabled(true);
        shop.setCustomer(customer);
        Shop updateShop = shopRepository.save(shop);
        return updateShop;

    }

    // tìm kiếm shop
    public Optional<Shop> findByIdShop(Integer id){
        return shopRepository.findById(id);
    }




}
