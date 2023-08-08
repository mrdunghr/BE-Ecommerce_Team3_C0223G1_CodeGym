package com.team3.ecommerce.request;

import com.team3.ecommerce.entity.Customer;
import com.team3.ecommerce.entity.order.OrderDetail;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderRequest {
    private Customer customer;
    private OrderDetail orderDetail;
}
