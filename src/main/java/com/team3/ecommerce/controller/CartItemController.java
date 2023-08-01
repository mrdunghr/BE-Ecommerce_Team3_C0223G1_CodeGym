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
    // xóa sản phẩm khỏi giỏ hàng
    @DeleteMapping("/delete-product/{id_cartItem}")
    public ResponseEntity<String> deleteProduct(@PathVariable Integer id_cartItem) {
        // Kiểm tra nếu id_cartItem không hợp lệ (null) thì trả về mã lỗi 400 Bad Request
        if (id_cartItem == null) {
            return ResponseEntity.badRequest().build();
        }

        // Kiểm tra xem cart item có tồn tại trong cartItemService hay không
        if (!cartItemService.exists(id_cartItem)) {
            // Nếu không tìm thấy cart item với id_cartItem, trả về mã lỗi 404 Not Found
            return ResponseEntity.notFound().build();
        }

        // Xóa cart item dựa trên id_cartItem và cập nhật vào cartItemService
        cartItemService.deleteCartItemById(id_cartItem);

        return ResponseEntity.ok("Delete success");
    }
}
