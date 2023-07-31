package com.team3.ecommerce.controller;

import com.team3.ecommerce.entity.order.Order;
import com.team3.ecommerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
@CrossOrigin("*")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/create-order/{id}")
    public ResponseEntity<Order> createOrder(@PathVariable Integer id){
        Order order = orderService.createOrder(id);
        return ResponseEntity.ok(order);
    }

}