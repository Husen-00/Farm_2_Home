package com.farmerconnect.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/reviews")
@Tag(name = "Product Ratings & Reviews", description = "Endpoints for produce ratings, buyer feedback, and quality reviews")
@CrossOrigin(origins = "*")
public class ReviewController {

    private final Map<String, List<Map<String, Object>>> productReviews = new HashMap<>();

    @GetMapping("/product/{productId}")
    @Operation(summary = "Get reviews for an agricultural product")
    public ResponseEntity<List<Map<String, Object>>> getReviews(@PathVariable String productId) {
        List<Map<String, Object>> list = productReviews.getOrDefault(productId, Collections.emptyList());
        return ResponseEntity.ok(list);
    }

    @PostMapping("/product/{productId}")
    @Operation(summary = "Submit a review and rating for produce")
    public ResponseEntity<Map<String, Object>> addReview(
            @PathVariable String productId,
            @RequestBody Map<String, Object> reviewData
    ) {
        String id = "REV-" + UUID.randomUUID().toString().substring(0, 8);
        reviewData.put("id", id);
        reviewData.put("date", new Date().toString());

        productReviews.computeIfAbsent(productId, k -> new ArrayList<>()).add(0, reviewData);
        return ResponseEntity.ok(reviewData);
    }
}
