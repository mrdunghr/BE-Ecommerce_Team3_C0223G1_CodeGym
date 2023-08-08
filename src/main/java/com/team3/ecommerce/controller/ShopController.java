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

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
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
    public ResponseEntity<?> createShop(@RequestBody Shop shop) {
        // Lấy thông tin khách hàng từ đối tượng Shop
        Customer customer = shop.getCustomer();
        // Kiểm tra xem người dùng đã có shop hay chưa
        List<Shop> existingShops = (List<Shop>) shopService.findShopList(customer);
        if (!existingShops.isEmpty()) {
            // Người dùng đã có shop, có thể hiển thị thông báo lỗi hoặc cơ hội chỉnh sửa shop hiện có
            return ResponseEntity.badRequest().body("You already have a shop.");
        }
        // Tạo đối tượng Shop mới
        Shop newShop = new Shop();
        newShop.setName(shop.getName());
        newShop.setAlias(shop.getAlias());
        newShop.setImage(shop.getImage());
        newShop.setDeliveryAddress(shop.getDeliveryAddress());
        newShop.setEnabled(false);
        newShop.setCreatedTime(new Date());
        newShop.setCustomer(customer);

        // Lưu đối tượng Shop mới vào cơ sở dữ liệu
        Shop savedShop = shopService.createShop(newShop);

        // Trả về thông tin Shop mới vừa tạo
        return ResponseEntity.ok(savedShop);
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

    // đóng shop
    @PutMapping("/{shopId}/close")
    public ResponseEntity<?> closeShop(
            @PathVariable Integer shopId
    ) {
        Optional<Shop> shop = shopService.findByIdShop(shopId);
        if (!shop.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        shop.get().setEnabled(false);
        shopService.createShop(shop.get());
        return ResponseEntity.ok().build();
    }

    // mở shop
    @PutMapping("/{shopId}/open")
    public ResponseEntity<?> openShop(
            @PathVariable Integer shopId
    ) {
        Optional<Shop> shop = shopService.findByIdShop(shopId);
        if (!shop.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        shop.get().setEnabled(true);
        shopService.createShop(shop.get());
        return ResponseEntity.ok().build();
    }

    // lấy dánh sách shop của customer theo page
    @GetMapping("/{customer_id}")
    public ResponseEntity<?> findShopByCustomer(@PathVariable Integer customer_id,
                                                @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "3") int size,
                                                @RequestParam(defaultValue = "false") boolean list) {
        Customer customer = customerService.getCustomerById(customer_id).orElse(null);
        if (customer != null && customer.isEnabled()) {
            if (list) {
                Iterable<Shop> shops = shopService.findShopList(customer);
                return ResponseEntity.ok(shops);
            } else {
                Pageable pageable = PageRequest.of(page, size);
                Page<Shop> shops = shopService.findShopByCustomer(customer, pageable);
                return new ResponseEntity<>(shops, HttpStatus.OK);
            }
        } else {
            return ResponseEntity.badRequest().body("Account does not exist or is disabled");
        }
    }

    //    Lấy một shop bằng id của shop
    @GetMapping("/get-shop/{id}")
    public ResponseEntity<Shop> getShop(@PathVariable int id) {
        return ResponseEntity.ok(shopService.findByIdShop(id).get());
    }

        @PutMapping("/{id}/enabled/{status}")
        public ResponseEntity<String> updateUserEnabledStatus (@PathVariable("id") Integer id,
        @PathVariable("status") boolean enabled){
            try {
                shopService.updateShopEnabledStatus(id, enabled);
                String status = enabled ? "enabled" : "disabled";
                String message = "The Shop ID " + id + " has been " + status;
                return ResponseEntity.ok(message);
            } catch (NoSuchElementException e) {
                return ResponseEntity.notFound().build();
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }

    }
