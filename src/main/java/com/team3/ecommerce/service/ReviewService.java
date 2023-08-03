package com.team3.ecommerce.service;

import com.team3.ecommerce.entity.Customer;
import com.team3.ecommerce.entity.Review;
import com.team3.ecommerce.repository.ProductRepository;
import com.team3.ecommerce.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private ProductRepository productRepo;
    public Iterable<Review> getAllReviewsByProductId(Integer productId) {
        return reviewRepository.findAllByProduct_Id(productId);
    }



    public boolean didCustomerReviewProduct(Customer customer, Integer productId) {
        Long count = reviewRepository.countByCustomerAndProduct(customer.getId(), productId);
        return count > 0;
    }


    // tạo reviews mới
    public Review saveReview(Review review) {
        review.setReviewTime(new Date());
        Review savedReview = reviewRepository.save(review);
        Integer productId = savedReview.getProduct().getId();
        productRepo.updateReviewCountAndAverageRating(productId);// cập nhập lại product
        return savedReview;
    }
}
