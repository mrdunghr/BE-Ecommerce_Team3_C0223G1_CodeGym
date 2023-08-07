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
    public ResponseEntity<List<CartItem>> viewCart(@PathVariable Integer id) {
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
        if (!productService.findById(cartItem.getProduct().getId()).get().isEnabled()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        // kiểm tra xem món hàng có thuộc quyền sở hữu của người mua hay không
        Product product = productService.findById(cartItem.getProduct().getId()).get();
        if (product.getCustomer().getId().equals(customerId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        // lấy danh sách các mặt hàng có trong giỏ hàng của khách hàng dựa vào customerId và productId
        List<CartItem> cartItems = cartItemService.cardItemRepository.findByCustomerId(customerId);
        if (cartItem.getQuantity() == 0) {
            cartItem.setQuantity(1);
        }
        if (!cartItems.isEmpty()) {
            for (CartItem item : cartItems) {
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

        switch (action) {
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
    public ResponseEntity<?> checked(@RequestBody CartItem cartItem) {
        if (cartItem.isChecked() == true) {
            cartItem.setChecked(false);
        } else {
            cartItem.setChecked(true);
        }
        cartItemService.saveCartItem(cartItem);
        return ResponseEntity.ok("OK");
    }

    @PutMapping("/checked-all-item/{checked}/{customerId}")
    public ResponseEntity<?> checkedAll(@PathVariable String checked, @PathVariable Integer customerId) {
        List<CartItem> list = cartItemService.getCartItemByCustomerId(customerId);
        switch (checked) {
            case "checked":
                for (CartItem item : list) {
                    item.setChecked(true);
                }
                cartItemService.saveAll(list);
                break;
            case "unchecked":
                for (CartItem item : list) {
                    item.setChecked(false);
                }
                cartItemService.saveAll(list);
                break;
        }
        return ResponseEntity.ok("OK");
    }

    // xóa sản phẩm khỏi giỏ hàng
    @DeleteMapping("/remove-item/{itemId}")
    public ResponseEntity<?> removeItemfromCart(@PathVariable Integer itemId){
        cartItemService.deleteCartItemById(itemId);
        return ResponseEntity.ok("OK");
    }
}
