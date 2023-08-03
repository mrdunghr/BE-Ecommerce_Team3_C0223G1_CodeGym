package com.team3.ecommerce.controller;

import com.team3.ecommerce.entity.Customer;
import com.team3.ecommerce.entity.Review;
import com.team3.ecommerce.entity.ReviewRequest;
import com.team3.ecommerce.entity.product.Product;
import com.team3.ecommerce.service.CustomerService;
import com.team3.ecommerce.service.ProductService;
import com.team3.ecommerce.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

@RequestMapping("/api/v1/reviews")
 @CrossOrigin("*")
@RestController
public class ReviewController{
@Autowired
    private ReviewService reviewService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CustomerService customerService;

    @GetMapping("/{productId}")
    public ResponseEntity<?> getAllReviewsByProductId(@PathVariable Integer productId) {
        Iterable<Review> reviews = reviewService.getAllReviewsByProductId(productId);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @PostMapping("/{productId}/{customerId}/create")
    public ResponseEntity<?> createReview(@PathVariable Integer productId,
                                          @RequestBody Review review,
                                          @PathVariable Integer customerId) {
        Optional<Product> product= productService.findById(productId);
        Optional<Customer> customer =customerService.findById(customerId);
        boolean customerReviewed = reviewService.didCustomerReviewProduct(customer.get(), product.get().getId());
        if (customerReviewed) {
            return ResponseEntity.badRequest().body("Customer has already reviewed this product.");
        }
        review.setProduct(product.get());
        review.setCustomer(customer.get());
        reviewService.saveReview(review);
        return ResponseEntity.ok(review);
    }


}
