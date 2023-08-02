package com.team3.ecommerce.controller;

import com.team3.ecommerce.entity.order.Order;
import com.team3.ecommerce.service.OrderDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payment")
@CrossOrigin("*")
public class OrderController {
    @Autowired
    private OrderDetailsService detailService;

    @PostMapping("/create-order/{id}")
    public ResponseEntity<?> createOrder(@PathVariable Integer id){
        Order order = null;
        try {
            order = detailService.createOrder(id);
        } catch (Exception e) {
            return new ResponseEntity<>(detailService.getDisableList(id), HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok(order);
    }
}