package com.team3.ecommerce.controller;

import com.team3.ecommerce.entity.Customer;
import com.team3.ecommerce.entity.Review;
import com.team3.ecommerce.entity.ReviewRequest;
import com.team3.ecommerce.entity.order.OrderDetail;
import com.team3.ecommerce.entity.product.Product;
import com.team3.ecommerce.service.CustomerService;
import com.team3.ecommerce.service.OrderDetailsService;
import com.team3.ecommerce.service.ProductService;
import com.team3.ecommerce.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CustomerService customerService;

    @Autowired
    private OrderDetailsService orderDetailsService;

    @GetMapping("/{productId}")
    public ResponseEntity<?> getAllReviewsByProductId(@PathVariable Integer productId) {
        Iterable<Review> reviews = reviewService.getAllReviewsByProductId(productId);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @PostMapping("/{productId}/{customerId}/create")
    public ResponseEntity<?> createReview(@PathVariable Integer productId,
                                          @RequestBody ReviewRequest reviewRequest,
                                          @PathVariable Integer customerId) {
        Optional<Product> product= productService.findById(productId);
        Optional<Customer> customer =customerService.findById(customerId);
        Review review = new Review();
        review.setComment(reviewRequest.getComment());
        review.setRating(reviewRequest.getRating());
        review.setProduct(product.get());
        review.setCustomer(customer.get());
        review.setReviewTime(new Date());
        review.setHeadline(" ");

        review = reviewService.savReview(review);
        return ResponseEntity.ok(review);
    }

    @PostMapping("/comment/{orderDetailsId}")
    public ResponseEntity<?> comment(@RequestBody Review review, @PathVariable Integer orderDetailsId){
        OrderDetail orderDetail = orderDetailsService.getOrderById(orderDetailsId);
        Product p = review.getProduct();
        List<Review> reviews = (List<Review>) reviewService.getAllReviewsByProductId(p.getId());
        review.setReviewTime(new Date());
        if (reviews.isEmpty()){
            p.setAverageRating(review.getRating());
            reviewService.savReview(review);
        }else {
            int rating = review.getRating();
            for (Review r : reviews) {
                rating += r.getRating();
            }
            DecimalFormat df = new DecimalFormat("#.#");
            float afterRate = Float.parseFloat(df.format(rating / reviews.size()));
            p.setAverageRating(afterRate);
            p.setReviewCount(p.getReviewCount() + 1);
            orderDetail.setHasReviewed(true);
            orderDetailsService.saveOrder(orderDetail);
            productService.editProduct(p);
            reviewService.savReview(review);
        }
        return ResponseEntity.ok(review);
    }

}
