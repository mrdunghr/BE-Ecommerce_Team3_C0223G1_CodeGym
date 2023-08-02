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
    @PostMapping("/{id}/cancel")
    public ResponseEntity<String> cancelOrder(@PathVariable Integer id) {
        OrderDetail orderDetail = orderDetailsService.findById(id).get();
        if (orderDetail == null) {
            return new ResponseEntity<>("Order not found!", HttpStatus.NOT_FOUND);
        }
        if (orderDetail.getStatus() == OrderStatus.NEW) {
            orderDetail.setStatus(OrderStatus.CANCELLED);
            orderDetailsService.saveOrderDetail(orderDetail);
            return ResponseEntity.status(HttpStatus.OK).body("Order cancelled successfully!");
        } else {
            return ResponseEntity.badRequest().body("Cannot cancel order, it is not in NEW status!");
        }
    }
    @PostMapping("/{orderId}/process")
    public ResponseEntity<String> processOrder(@PathVariable Integer orderId) {
        OrderDetail orderToUpdate = orderDetailsService.findById(orderId).get();
        if (orderToUpdate == null) {
            return new ResponseEntity<>("Order not found!", HttpStatus.NOT_FOUND);
        }

        if (orderToUpdate.getStatus() == OrderStatus.CANCELLED) {
            // Đơn hàng đã bị hủy, không thể tiếp tục xử lý
            return new ResponseEntity<>("Cannot process order, it is already cancelled!", HttpStatus.BAD_REQUEST);
        } else if (orderToUpdate.getStatus() == OrderStatus.NEW) {
            // Kiểm tra trạng thái trong kho
            boolean isInStock = orderToUpdate.getProduct().isInStock();

            if (!isInStock) {
                // Tiến hành xử lý đơn hàng
                orderToUpdate.setStatus(OrderStatus.PROCESSING);
                orderDetailsService.saveOrderDetail(orderToUpdate);
                return new ResponseEntity<>("Order is being processed!", HttpStatus.OK);
            } else {
                // Không còn hàng trong kho, hủy đơn hàng
                orderToUpdate.setStatus(OrderStatus.CANCELLED);
                orderDetailsService.saveOrderDetail(orderToUpdate);
                return new ResponseEntity<>("Order cancelled due to out of stock!", HttpStatus.BAD_REQUEST);
            }
        } else {
            // Đơn hàng không ở trạng thái NEW hoặc CANCELLED, không thể tiếp tục xử lý
            return new ResponseEntity<>("Cannot process order, it is not in NEW status!", HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/{orderId}/paid")
    public ResponseEntity<String> paidOrder(@PathVariable Integer orderId) {
        OrderDetail orderUpdate = orderDetailsService.findById(orderId).get();
        if (orderUpdate == null) {
            return new ResponseEntity<>("Order not found!", HttpStatus.NOT_FOUND);
        }
        if (orderUpdate.getStatus() == OrderStatus.PROCESSING) {
            orderUpdate.setStatus(OrderStatus.PAID);
            orderDetailsService.saveOrderDetail(orderUpdate);
            return new ResponseEntity<>("Order is being paid!",HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Cannot paid order", HttpStatus.BAD_REQUEST);
        }
    }

}