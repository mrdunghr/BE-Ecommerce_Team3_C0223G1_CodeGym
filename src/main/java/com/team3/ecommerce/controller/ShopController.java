package com.team3.ecommerce.controller;

import com.team3.ecommerce.entity.Customer;
import com.team3.ecommerce.entity.Shop;
import com.team3.ecommerce.repository.IShopRepository;
import com.team3.ecommerce.service.CustomerService;
import com.team3.ecommerce.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/shop")
public class ShopController {

    @Autowired
    private ShopService shopService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private IShopRepository iShopRepository;

    // hiển thị tất cả các shop
    @GetMapping("/list")
    public ResponseEntity<Iterable<Shop>> findAll(){
          Iterable<Shop> shop = shopService.findAllShop();
          return new ResponseEntity<>(shop, HttpStatus.OK);
    }

    // tạo shop mới
    @PostMapping("/create")
    public ResponseEntity<Shop> createShop(@RequestBody Shop shop,@RequestParam Integer customerId){
        Customer customer = customerService.getCustomerById(customerId).get();
        return new ResponseEntity<>(shopService.createShop(shop,customer),HttpStatus.OK);
    }
    // Tìm kiếm theo name shop
    @GetMapping("/search-by-name")
    public ResponseEntity<Iterable<Shop>> findShopsByName(@RequestParam("name") String name) {
        Iterable<Shop> shops = iShopRepository.findAllByNameContaining(name);
        if (shops.iterator().hasNext()) {
            return new ResponseEntity<>(shops, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
