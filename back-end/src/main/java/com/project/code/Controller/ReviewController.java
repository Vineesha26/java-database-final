package com.project.code.Controller;

import com.project.code.Model.Customer;
import com.project.code.Model.Review;
import com.project.code.Repo.CustomerRepository;
import com.project.code.Repo.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired private ReviewRepository reviewRepository;
    @Autowired private CustomerRepository customerRepository;

    @GetMapping("/{storeId}/{productId}")
    public Map<String, Object> getReviews(@PathVariable("storeId") Long storeId,
                                          @PathVariable("productId") Long productId) {
        Map<String, Object> resp = new HashMap<>();

        List<Review> reviews = reviewRepository.findByStoreIdAndProductId(storeId, productId);

        List<Map<String, Object>> reduced = reviews.stream().map(r -> {
            Map<String, Object> row = new HashMap<>();
            row.put("comment", r.getComment());
            row.put("rating", r.getRating());

            Customer c = customerRepository.findById(r.getCustomerId()).orElse(null);
            row.put("customerName", (c != null && c.getName() != null) ? c.getName() : "Unknown");
            return row;
        }).collect(Collectors.toList());

        resp.put("reviews", reduced);
        return resp;
    }
}
