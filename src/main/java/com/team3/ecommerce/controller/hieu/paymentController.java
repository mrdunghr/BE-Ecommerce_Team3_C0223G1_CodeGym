package com.team3.ecommerce.controller.hieu;

import com.team3.ecommerce.entity.order.Order;
import com.team3.ecommerce.service.hieu.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
@CrossOrigin("*")
public class paymentController {
    @Autowired
    private OrderDetailService detailService;

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
