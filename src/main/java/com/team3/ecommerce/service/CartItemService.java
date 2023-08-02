package com.team3.ecommerce.service;

import com.team3.ecommerce.entity.CartItem;
import com.team3.ecommerce.repository.CardItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemService {
    @Autowired
    public CardItemRepository cardItemRepository;

    public CartItem saveCartItem(CartItem cartItem){
        return cardItemRepository.save(cartItem);
    }
    public List<CartItem> getAllCartItems() {
        return cardItemRepository.findAll();
    }
    public List<CartItem> getCartItemByCustomerId(Integer id){
        return cardItemRepository.findByCustomerId(id);
    }
    public void deleteCartItem(CartItem cartItem) {

        cardItemRepository.delete(cartItem);
    }
    public void saveAll(List<CartItem> items){
        cardItemRepository.saveAll(items);
    }
    public void deleteCartItemById(int id){
        cardItemRepository.deleteById(id);
    }
}
