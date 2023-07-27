package com.team3.ecommerce.controller;

import com.team3.ecommerce.entity.CartItem;
import com.team3.ecommerce.entity.Customer;
import com.team3.ecommerce.entity.product.Product;
import com.team3.ecommerce.service.CartItemService;
import com.team3.ecommerce.service.CustomerService;
import com.team3.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/cart-item")
public class CartItemController {

    @Autowired
    private CartItemService cartItemService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ProductService productService;
    @GetMapping("/view")
    public ResponseEntity<List<CartItem>> viewCart(HttpSession session){
        Integer customerId = (Integer) session.getAttribute("customerId");
        if (customerId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<CartItem> cartItems = cartItemService.getCartItemByCustomerId(customerId);
        return ResponseEntity.ok(cartItems);
    }

    @PostMapping("/add")
    public ResponseEntity<CartItem> addToCart(@RequestBody CartItem cartItem, HttpSession session) {
        Integer customerId = (Integer) session.getAttribute("customerId");
        if (customerId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
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
}
