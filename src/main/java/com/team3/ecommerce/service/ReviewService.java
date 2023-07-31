package com.team3.ecommerce.service;

import com.team3.ecommerce.entity.Review;
import com.team3.ecommerce.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;
    public Iterable<Review> getAllReviewsByProductId(Integer productId) {
        return reviewRepository.findAllByProduct_Id(productId);
    }

}
