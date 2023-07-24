package com.team3.ecommerce.controller;

import com.team3.ecommerce.entity.Customer;
import com.team3.ecommerce.entity.Shop;
import com.team3.ecommerce.repository.IShopRepository;
import com.team3.ecommerce.service.CustomerService;
import com.team3.ecommerce.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    @Autowired
    private IShopRepository iShopRepository;

    // hiển thị tất cả các shop
    @GetMapping("/list")
    public ResponseEntity<Page<Shop>> findAll(@RequestParam(name = "page", defaultValue = "0") int page,
                                              @RequestParam(name = "size", defaultValue = "3") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Shop> shop = shopService.findAllShop(pageable);
        return new ResponseEntity<>(shop, HttpStatus.OK);
    }

    // tạo shop mới
    @PostMapping("/create")
    public ResponseEntity<Shop> createShop(@RequestBody Shop shop, @RequestParam Integer customerId) {
        Customer customer = customerService.getCustomerById(customerId).get();
        return new ResponseEntity<>(shopService.createShop(shop, customer), HttpStatus.OK);
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

    // chỉnh sửa thông tin của shop
    @PutMapping("/edit-shop/{shopId}")
    public ResponseEntity<Optional<Shop>> editShop(@PathVariable Integer shopId, @RequestBody Shop shop) {
        Optional<Shop> shop1 = shopService.findByIdShop(shopId);
        if (!shop1.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        shop.setId(shop1.get().getId());
        shopService.updateShop(shop);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // lấy dánh sách shop của customer
    @GetMapping("/{customer_id}")
    public ResponseEntity<?> findShopByCustomer(@PathVariable Integer customer_id,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "5") int size) {
        Customer customer = customerService.getCustomerById(customer_id).orElse(null);
        if (customer != null && customer.isEnabled()) {
            Pageable pageable = PageRequest.of(page, size);
            Page<Shop> shops = shopService.findShopByCustomer(customer, pageable);
            return new ResponseEntity<>(shops, HttpStatus.OK);
        } else {
            return ResponseEntity.badRequest().body("tài khoản không tồn tại hoặc bị vô hiệu hóa");
        }
    }

}
