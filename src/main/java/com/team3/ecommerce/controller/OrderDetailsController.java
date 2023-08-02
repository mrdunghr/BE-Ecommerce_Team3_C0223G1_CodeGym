package com.team3.ecommerce.controller;

import com.team3.ecommerce.entity.order.Order;
import com.team3.ecommerce.entity.order.OrderDetail;
import com.team3.ecommerce.entity.order.OrderStatus;
import com.team3.ecommerce.entity.product.Product;
import com.team3.ecommerce.service.OrderDetailsService;
import com.team3.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order-details")
@CrossOrigin("*")
public class OrderDetailsController {
    @Autowired
    private OrderDetailsService oderDetailsService;

    @Autowired
    private ProductService productService;
    @GetMapping("/shop/{id}")
    public ResponseEntity<List<OrderDetail>> getListOrderByShopId(@PathVariable Integer id) {
        return ResponseEntity.ok(oderDetailsService.getListOrderByShopId(id));
    }
    @GetMapping("/{customerId}")
    public ResponseEntity<List<Order>> getCustomerOrder(@PathVariable Integer customerId){
        return new ResponseEntity<>(oderDetailsService.getListOrderDetailByCustomerId(customerId), HttpStatus.OK);
    }

    @PutMapping("/confirm-order/{action}/{id}")
    public ResponseEntity<?> confirmOrder(@PathVariable String action ,@PathVariable Integer id){
        OrderDetail confirmOrder = oderDetailsService.getOrderById(id);
        int order_quan = confirmOrder.getQuantity();
        int product_quan = confirmOrder.getProduct().getQuantity();
        switch (action) {
            case "confirm":
                if(order_quan > product_quan){
                    return new ResponseEntity<>("The quantity is not enough!", HttpStatus.BAD_REQUEST);
                }else {
                    confirmOrder.setStatus(OrderStatus.PROCESSING);
                    Product p = confirmOrder.getProduct();
                    p.setQuantity(product_quan - order_quan); oderDetailsService.saveOrder(confirmOrder);
                    productService.editProduct(p);
                    return new ResponseEntity<>(HttpStatus.OK);
                }
            case "paid":
                confirmOrder.setStatus(OrderStatus.PAID); oderDetailsService.saveOrder(confirmOrder);
                return new ResponseEntity<>(HttpStatus.OK);
            case "return":
                confirmOrder.setStatus(OrderStatus.RETURNED); oderDetailsService.saveOrder(confirmOrder);
                Product p = confirmOrder.getProduct();
                p.setQuantity(product_quan + order_quan);
                productService.editProduct(p);
                return new ResponseEntity<>(HttpStatus.OK);
            case "cancel":
                confirmOrder.setStatus(OrderStatus.CANCELLED); oderDetailsService.saveOrder(confirmOrder);
                return new ResponseEntity<>(HttpStatus.OK);
            default:
                return ResponseEntity.ok("OK");
        }
    }
}