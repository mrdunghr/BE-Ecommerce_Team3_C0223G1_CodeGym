package com.team3.ecommerce.service;

import com.team3.ecommerce.entity.CartItem;
import com.team3.ecommerce.entity.Customer;
import com.team3.ecommerce.entity.order.*;
import com.team3.ecommerce.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderService {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private CartItemService cartItemService;
    @Autowired
    private OrderRepository orderRepository;
    public Order createOrder(int id){
        Customer customer =customerService.getCustomerById(id).get();
        Order order = new Order();
        List<CartItem> cartItemList = cartItemService.getCartItemByCustomerId(id);
        Set<OrderDetail> orderDetailSet = new HashSet<>();

        float product_cost = 0;
        float sub_total = 0;
        for(CartItem item : cartItemList){
            if(item.isChecked()){
                OrderDetail orderDetail = new OrderDetail(customer);
                orderDetail.setOrder(order);
                orderDetail.setCustomer(item.getCustomer());
                orderDetail.setProduct(item.getProduct());
                orderDetail.setQuantity(item.getQuantity());
                orderDetail.setSubtotal(item.getSubtotal());
                orderDetail.setProductCost(item.getProduct().getCost() * item.getQuantity());
                orderDetail.setShippingCost(item.getShippingCost());
                orderDetail.setStatus(OrderStatus.NEW);
                orderDetail.setUnitPrice(orderDetail.getSubtotal() - orderDetail.getQuantity() * item.getProduct().getDiscountPercent()/100);
                orderDetailSet.add(orderDetail);
                product_cost += orderDetail.getProductCost();
                sub_total += orderDetail.getSubtotal();
//                cartItemService.deleteCartItem(item);
            }
        }
        order.setOrderDetails(orderDetailSet);
        order.setOrderTracks(new ArrayList<OrderTrack>());
        order.setCustomer(customer);
        order.setCountry(customer.getCountry().getName());
        order.setPaymentMethod(PaymentMethod.COD);
        order.setStatus(OrderStatus.NEW);
        order.setCity(customer.getCity());
        order.setFirstName(customer.getFirstName());
        order.setLastName(customer.getLastName());
        order.setPhoneNumber(customer.getPhoneNumber());
        order.setState(customer.getState());
        order.setPostalCode(customer.getPostalCode());
        order.setAddressLine1(customer.getAddressLine1());
        order.setAddressLine2(customer.getAddressLine2());
        order.setOrderTime(new Date());
        order.setDeliverDate(new Date());

        order.setProductCost(product_cost);
        order.setSubtotal(sub_total);
        order.setTotal(order.getSubtotal() + order.getProductCost() + order.getTax() + order.getShippingCost());
        orderRepository.save(order);

        return order;
    }
}
