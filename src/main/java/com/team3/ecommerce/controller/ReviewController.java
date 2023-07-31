package com.team3.ecommerce.controller;

import com.team3.ecommerce.entity.Review;
import com.team3.ecommerce.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @GetMapping("/{productId}")
    public ResponseEntity<?> getAllReviewsByProductId(@PathVariable Integer productId) {
        Iterable<Review> reviews = reviewService.getAllReviewsByProductId(productId);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

}
