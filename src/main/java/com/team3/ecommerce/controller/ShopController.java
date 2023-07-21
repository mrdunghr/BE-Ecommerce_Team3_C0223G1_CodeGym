package com.team3.ecommerce.controller;

import com.team3.ecommerce.entity.Customer;
import com.team3.ecommerce.entity.Shop;
import com.team3.ecommerce.service.CustomerService;
import com.team3.ecommerce.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/shop")
public class ShopController {

    @Autowired
    private ShopService shopService;
    @Autowired
    private CustomerService customerService;

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

    // chỉnh sửa thông tin của shop
    @PutMapping("/edit-shop/{shopId}")
    public ResponseEntity<Optional<Shop>> editShop(@PathVariable Integer shopId,@RequestBody Shop shop){
        Optional<Shop> shop1= shopService.findByIdShop(shopId);
        if(!shop1.isPresent()){
               return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
       shop.setId(shop1.get().getId());
        shopService.updateShop(shop);
        return new ResponseEntity<>(HttpStatus.OK);
    }



}
