package com.team3.ecommerce.controller;

import com.team3.ecommerce.entity.order.OrderDetail;
import com.team3.ecommerce.service.OrderDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order-details")
@CrossOrigin("*")
public class OrderDetailsController {
    @Autowired
    private OrderDetailsService orderDetailsService;

    @GetMapping("/{id}")
    public ResponseEntity<List<OrderDetail>> getListOrderByShopId(@PathVariable Integer id) {
        return ResponseEntity.ok(orderDetailsService.getListOrderByShopId(id));
    }
}