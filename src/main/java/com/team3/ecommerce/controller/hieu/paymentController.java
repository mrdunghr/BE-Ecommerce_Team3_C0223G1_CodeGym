package com.team3.ecommerce.controller.hieu;

import com.team3.ecommerce.entity.Customer;
import com.team3.ecommerce.entity.order.Order;
import com.team3.ecommerce.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
@CrossOrigin("*")
public class paymentController {
    @Autowired
    private OrderDetailService detailService;

    @PostMapping("/create-order/{id}")
    public ResponseEntity<Order> createOrder(@PathVariable Integer id){
        Order order = detailService.createOrder(id);
        return ResponseEntity.ok(order);
    }

}
