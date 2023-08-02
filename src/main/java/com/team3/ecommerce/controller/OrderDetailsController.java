package com.team3.ecommerce.controller;

import com.team3.ecommerce.entity.order.OrderDetail;
import com.team3.ecommerce.entity.order.OrderStatus;
import com.team3.ecommerce.service.OrderDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order-details")
public class OrderDetailsController {
    @Autowired
    private OrderDetailsService orderDetailsService;

    @GetMapping("/{id}")
    public ResponseEntity<List<OrderDetail>> getListOrderByShopId(@PathVariable Integer id) {
        return ResponseEntity.ok(orderDetailsService.getListOrderByShopId(id));
    }
    // thay đổi trạng thái đơn hàng
    @PostMapping("/{orderId}/update_status")
    public ResponseEntity<String> updateOrderStatus(@PathVariable Integer orderId, @RequestParam("action") String action) {
        OrderDetail orderToUpdate = orderDetailsService.findById(orderId).orElse(null);
        if (orderToUpdate == null) {
            return new ResponseEntity<>("Order not found!", HttpStatus.NOT_FOUND);
        }

        switch (action) {
            // hủy đơn
            case "cancel":
                if (orderToUpdate.getStatus() == OrderStatus.NEW) {
                    orderToUpdate.setStatus(OrderStatus.CANCELLED);
                    orderDetailsService.saveOrderDetail(orderToUpdate);
                    return ResponseEntity.ok("Order cancelled successfully!");
                } else {
                    return ResponseEntity.badRequest().body("Cannot cancel order, it is not in NEW status!");
                }
                //thực hiện đơn hàng
            case "process":
                if (orderToUpdate.getStatus() == OrderStatus.CANCELLED) {
                    return new ResponseEntity<>("Cannot process order, it is already cancelled!", HttpStatus.BAD_REQUEST);
                } else if (orderToUpdate.getStatus() == OrderStatus.NEW) {
                    boolean isInStock = orderToUpdate.getProduct().isInStock();
                    if (!isInStock) {
                        orderToUpdate.setStatus(OrderStatus.PROCESSING);
                        orderDetailsService.saveOrderDetail(orderToUpdate);
                        return ResponseEntity.ok("Order is being processed!");
                    } else {
                        orderToUpdate.setStatus(OrderStatus.CANCELLED);
                        orderDetailsService.saveOrderDetail(orderToUpdate);
                        return new ResponseEntity<>("Order cancelled due to out of stock!", HttpStatus.BAD_REQUEST);
                    }
                } else {
                    return new ResponseEntity<>("Cannot process order, it is not in NEW status!", HttpStatus.BAD_REQUEST);
                }
            case "return":
                if (orderToUpdate.getStatus() == OrderStatus.PROCESSING) {
                    orderToUpdate.setStatus(OrderStatus.RETURNED);
                    orderDetailsService.saveOrderDetail(orderToUpdate);
                    return ResponseEntity.ok("Order is being return!");
                }
                else {
                    return ResponseEntity.badRequest().body("Cannot return order");
                }
                // đơn hàng thành công
            case "paid":
                if (orderToUpdate.getStatus() == OrderStatus.PROCESSING) {
                    orderToUpdate.setStatus(OrderStatus.PAID);
                    orderDetailsService.saveOrderDetail(orderToUpdate);
                    return ResponseEntity.ok("Order is being paid!");
                } else {
                    return ResponseEntity.badRequest().body("Cannot pay for the order, it is not in PROCESSING status!");
                }
            default:
                return ResponseEntity.badRequest().body("Invalid action!");
        }
    }
}