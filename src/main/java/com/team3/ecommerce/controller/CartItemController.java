package com.team3.ecommerce.controller;

import com.team3.ecommerce.entity.CartItem;
import com.team3.ecommerce.entity.Customer;
import com.team3.ecommerce.service.CartItemService;
import com.team3.ecommerce.service.CustomerService;
import com.team3.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
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
        if (!productService.productIsExisted(cartItem.getProduct().getId())) {
            return ResponseEntity.notFound().build();
        }
        if (cartItem.getProduct().isEnabled()) {
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
    // cập nhật số lượng sản phẩm trong giỏ hàng
    @PostMapping("/update-quantities")
    public ResponseEntity<List<CartItem>> updateCartItemQuantities(@RequestBody List<CartItem> cartItemList, HttpSession session) {
        Integer customerId = (Integer) session.getAttribute("customerId");
        if (customerId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<CartItem> updateCartItems = new ArrayList<>();
        for (CartItem cartItem : cartItemList) {
            // truy xuất danh sách mặt hàng trong giỏ hàng của khách dựa vào customerId
            List<CartItem> cartItems = cartItemService.getCartItemByCustomerId(customerId);
            if (!cartItems.isEmpty()) {
                // lặp qua danh sách các mặt hàng trong giỏ hàng
                for (CartItem itemInCart : cartItems) {
                    if (cartItem.getProduct().getId().equals(itemInCart.getProduct().getId())) {
                        // cập nhật số lượng của mặt hàng cụ thể
                        itemInCart.setQuantity(cartItem.getQuantity());
                        // lưu vào cơ sở dữ liệu
                        cartItemService.saveCartItem(itemInCart);
                        updateCartItems.add(itemInCart);
                        break;
                    }
                }
            } else {
            }
        }
        return ResponseEntity.ok(updateCartItems);
    }
    // xóa sản phẩm khỏi giỏ hàng
    @DeleteMapping("/delete-product")
    public ResponseEntity<List<CartItem>> deleteProduct(@RequestParam Integer productId, HttpSession session) {
        Integer customerId = (Integer) session.getAttribute("customerId");
        if (customerId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<CartItem> cartItems = cartItemService.getCartItemByCustomerId(customerId);
        int productIndexToRemove = -1;
        for (int i = 0; i < cartItems.size(); i++) {
            if (cartItems.get(i).getProduct().getId().equals(productId)) {
                productIndexToRemove = i;
                break;
            }
        }
        if (productIndexToRemove != -1) {
            cartItemService.deleteCartItem(cartItems.get(productIndexToRemove));
            cartItems.remove(cartItems.get(productIndexToRemove));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(cartItems);
        }
        return ResponseEntity.ok(cartItems);
    }

}
