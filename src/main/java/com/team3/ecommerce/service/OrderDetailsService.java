package com.team3.ecommerce.service;

import com.team3.ecommerce.entity.order.OrderDetail;
import com.team3.ecommerce.repository.OrderDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class OrderDetailsService {
    @Autowired
    private OrderDetailsRepository iOrderDetailsRepository;

    public List<OrderDetail> getListOrderByShopId(Integer id){
        return iOrderDetailsRepository.findOrderDetailByShopId(id);
    }
}
