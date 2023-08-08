package com.team3.ecommerce.service;

import com.team3.ecommerce.entity.CartItem;
import com.team3.ecommerce.entity.Customer;
import com.team3.ecommerce.entity.Notification;
import com.team3.ecommerce.entity.NotificationType;
import com.team3.ecommerce.entity.order.*;
import com.team3.ecommerce.repository.OrderDetailsRepository;
import com.team3.ecommerce.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderDetailsService {

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartItemService cartItemService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private NotificationStorageService notificationStorageService;

    public Order createOrder(int id) throws Exception{
        Customer customer =customerService.getCustomerById(id).get();
        Order order = new Order();
        List<CartItem> cartItemList = cartItemService.getCartItemByCustomerId(id);
        Set<OrderDetail> orderDetailSet = new HashSet<>();
        Set<OrderDetail> orderDetails = new HashSet<>();
        for(CartItem item : cartItemList){
            if(item.isChecked() && item.getProduct().isEnabled()){
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setProduct(item.getProduct());
                orderDetail.setQuantity(item.getQuantity());
                orderDetail.setSubtotal(12);
                orderDetail.setProductCost(item.getProduct().getCost());
                orderDetail.setShippingCost(12);
                orderDetail.setUnitPrice(item.getProduct().getPrice() - (item.getProduct().getPrice() * item.getProduct().getDiscountPrice()/100));
                orderDetailSet.add(orderDetail);
                orderDetail.setCustomer(item.getCustomer());
                orderDetail.setStatus(OrderStatus.NEW);
                cartItemService.deleteCartItem(item);
                // Tạo thông báo
                notificationStorageService.createNotificationStorage(Notification.builder()
                        .delivered(false)
                        .content("Đơn hàng mới từ: " + customer.getFullName())
                        .notificationType(NotificationType.NEW)
                        .customerFrom(customer)
                        .customerTo(item.getProduct().getCustomer()).build());
            }
        }
//        order.setOrderDetails(orderDetailSet);
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
        orderRepository.save(order);
        Order order1 = orderRepository.findLatestOrderByCustomerId(id);
        for (OrderDetail od : orderDetailSet){
            od.setOrder(order1);
            orderDetails.add(od);
        }
        orderDetailsRepository.saveAll(orderDetails);
        return order;
    }
    public List<Order> getListOrderDetailByCustomerId(Integer customerId){
        return orderRepository.findAllByCustomer(customerId);
    }

    public List<CartItem> getDisableList(Integer customerId){
        List<CartItem> customerCartItem = cartItemService.getCartItemByCustomerId(customerId);
        List<CartItem> disabledItem = new ArrayList<>();
        for(CartItem item : customerCartItem){
            if (item.isChecked() && !item.getProduct().isEnabled()){
                disabledItem.add(item);
            }
        }
        return disabledItem;
    }

    public OrderDetail getOrderById(Integer id){
        return orderDetailsRepository.findById(id).get();
    }

    public void saveOrder(OrderDetail orderDetail){
        orderDetailsRepository.save(orderDetail);
    }
    public List<OrderDetail> getListOrderByShopId(Integer id){
        return orderDetailsRepository.findOrderDetailByShopId(id);
    }
}
