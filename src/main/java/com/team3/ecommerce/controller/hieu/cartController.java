package com.team3.ecommerce.controller.hieu;

import com.team3.ecommerce.entity.CartItem;
import com.team3.ecommerce.entity.Customer;
import com.team3.ecommerce.service.CartItemService;
import com.team3.ecommerce.service.CustomerService;
import com.team3.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/cart")
public class cartController {
    @Autowired
    private CartItemService cartItemService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ProductService productService;
    @GetMapping("/view/{id}")
    public ResponseEntity<List<CartItem>> viewCart(@PathVariable Integer id){
        if (id == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<CartItem> cartItems = cartItemService.getCartItemByCustomerId(id);
        return ResponseEntity.ok(cartItems);
    }
    @PostMapping("/add/{customerId}")
    public ResponseEntity<CartItem> addToCart(@RequestBody CartItem cartItem, @PathVariable Integer customerId) {
        if (customerId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (!productService.productIsExisted(cartItem.getProduct().getId())) {
            return ResponseEntity.notFound().build();
        }
        if (!cartItem.getProduct().isEnabled()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        // lấy danh sách các mặt hàng có trong giỏ hàng của khách hàng dựa vào customerId và productId
        List<CartItem> cartItems = cartItemService.cardItemRepository.findByCustomerId(customerId);
        if (cartItem.getQuantity() == 0) {
            cartItem.setQuantity(1);
        }
        if (!cartItems.isEmpty()) {
            for (CartItem item: cartItems) {
                if (item.getProduct().getId().equals(cartItem.getProduct().getId())) {
                    // nếu đã có sản phẩm thì tăng số lượng lên
                    item.setQuantity(item.getQuantity() + cartItem.getQuantity());
                    cartItemService.saveCartItem(item);
                    return ResponseEntity.ok(item);
                }
            }
        }
        Customer customer = customerService.getCustomerById(customerId).get();
        // mặt hàng chưa có thì thêm mới
        cartItem.setCustomer(customer);
        cartItem.setProduct(productService.findById(cartItem.getProduct().getId()).get());
        CartItem newCartItem = cartItemService.saveCartItem(cartItem);
        return ResponseEntity.ok(newCartItem);
    }
    @PutMapping("/update-quantities/{action}")
    public ResponseEntity<?> updateCartItemQuantities(@RequestBody CartItem cartItem, @PathVariable String action) {

        switch (action){
            case "increase":
                cartItem.setQuantity(cartItem.getQuantity() + 1);
                cartItemService.saveCartItem(cartItem);
                break;
            case "decrease":
                cartItem.setQuantity(cartItem.getQuantity() - 1);
                cartItemService.saveCartItem(cartItem);
                break;
        }
        return ResponseEntity.ok("Success!");
    }



    @PutMapping("/checked-item")
    public ResponseEntity<?> checked(@RequestBody CartItem cartItem){
        if (cartItem.isChecked() == true){
            cartItem.setChecked(false);
        }else{
            cartItem.setChecked(true);
        }
        cartItemService.saveCartItem(cartItem);
        return ResponseEntity.ok("OK");
    }
}
