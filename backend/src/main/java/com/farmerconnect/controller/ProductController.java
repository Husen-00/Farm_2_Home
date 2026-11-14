package com.farmerconnect.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Agricultural Marketplace", description = "Endpoints for farmers to list crops and buyers to browse produce")
@CrossOrigin(origins = "*")
public class ProductController {

    // In-memory backing store for instant demonstration & PostgreSQL JPA readiness
    private final List<Map<String, Object>> mockProducts = new ArrayList<>(Arrays.asList(
            createProductMap("PROD-01", "Organic Sona Masoori Paddy", "ಸಾವಯವ ಸೋನಾ ಮಸೂರಿ ಭತ್ತ", "CEREALS", 2450.0, "Quintal", 45.0, "Mandya, Karnataka", "Basavaraj Patil", "+91 98451 23456", true),
            createProductMap("PROD-02", "Finger Millet / Ragi (GPU-28)", "ಶುದ್ಧ ನಾಟಿ ರಾಗಿ (GPU-28)", "CEREALS", 3850.0, "Quintal", 30.0, "Hassan, Karnataka", "Mallikarjun Swamy", "+91 94482 67890", true),
            createProductMap("PROD-03", "Belagavi Red Round Onion", "ಬೆಳಗಾವಿ ಕೆಂಪು ಈರುಳ್ಳಿ", "VEGETABLES", 2200.0, "Quintal", 80.0, "Belagavi, Karnataka", "Shankar Gouda", "+91 99014 55432", false),
            createProductMap("PROD-04", "Farm Fresh Hybrid Tomatoes", "ತಾಜಾ ನಾಟಿ ಹೈಬ್ರಿಡ್ ಟೊಮ್ಯಾಟೊ", "VEGETABLES", 1450.0, "Quintal", 50.0, "Kolar, Karnataka", "Ramesh Kumar", "+91 97412 88990", false),
            createProductMap("PROD-05", "CO-86032 High-Sugar Sugarcane", "CO-86032 ಸಿಹಿ ಕಬ್ಬು", "COMMERCIAL", 3300.0, "Ton", 120.0, "Dharwad, Karnataka", "Chandrashekar", "+91 98801 12345", true),
            createProductMap("PROD-06", "Yellow Pigeon Pea (Toor Dal)", "ಕಲಬುರಗಿ ತೊಗರಿ ಕಾಳು", "PULSES", 9200.0, "Quintal", 25.0, "Kalaburagi, Karnataka", "Anand Rao", "+91 96113 45678", true)
    ));

    @GetMapping
    @Operation(summary = "Get all available agricultural produce", description = "Browse produce with optional search query and category filtering")
    public ResponseEntity<List<Map<String, Object>>> getProducts(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String category
    ) {
        if (search == null && category == null) {
            return ResponseEntity.ok(mockProducts);
        }

        List<Map<String, Object>> filtered = new ArrayList<>();
        for (Map<String, Object> p : mockProducts) {
            boolean matchSearch = search == null || p.get("titleEn").toString().toLowerCase().contains(search.toLowerCase())
                    || p.get("location").toString().toLowerCase().contains(search.toLowerCase());
            boolean matchCat = category == null || p.get("category").toString().equalsIgnoreCase(category);

            if (matchSearch && matchCat) {
                filtered.add(p);
            }
        }
        return ResponseEntity.ok(filtered);
    }

    @PostMapping
    @Operation(summary = "Publish a new produce listing", description = "Allows a verified farmer to post crop quantity, pricing, and grade")
    public ResponseEntity<Map<String, Object>> addProduct(@RequestBody Map<String, Object> productData) {
        String id = "PROD-" + System.currentTimeMillis() % 100000;
        productData.put("id", id);
        productData.put("rating", 5.0);
        mockProducts.add(0, productData);
        return ResponseEntity.ok(productData);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product details by ID")
    public ResponseEntity<Map<String, Object>> getProductById(@PathVariable String id) {
        return mockProducts.stream()
                .filter(p -> p.get("id").equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    private static Map<String, Object> createProductMap(
            String id, String titleEn, String titleKn, String category,
            Double price, String unit, Double quantity, String location,
            String farmerName, String farmerPhone, Boolean isOrganic
    ) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("titleEn", titleEn);
        map.put("titleKn", titleKn);
        map.put("category", category);
        map.put("price", price);
        map.put("unit", unit);
        map.put("availableQuantity", quantity);
        map.put("location", location);
        map.put("farmerName", farmerName);
        map.put("farmerPhone", farmerPhone);
        map.put("isOrganic", isOrganic);
        map.put("rating", 4.8);
        return map;
    }
}
