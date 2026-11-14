package com.farmerconnect.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Order Management", description = "Endpoints for placing and tracking agricultural produce orders")
@CrossOrigin(origins = "*")
public class OrderController {

    private final List<Map<String, Object>> mockOrders = new ArrayList<>(Arrays.asList(
            createOrderMap("ORD-94281", "PROD-01", "Organic Sona Masoori Paddy", "Sri Lakshmi Rice Mill", "+91 94800 11223", 20.0, "Quintal", 49000.0, "CONFIRMED", "Industrial Area, Mysore Road, Mandya")
    ));

    @GetMapping
    @Operation(summary = "Get user order history and status")
    public ResponseEntity<List<Map<String, Object>>> getOrders() {
        return ResponseEntity.ok(mockOrders);
    }

    @PostMapping
    @Operation(summary = "Place a new purchase order")
    public ResponseEntity<Map<String, Object>> placeOrder(@RequestBody Map<String, Object> orderPayload) {
        String orderId = "ORD-" + (System.currentTimeMillis() % 100000);
        orderPayload.put("id", orderId);
        orderPayload.put("status", "PLACED");
        orderPayload.put("createdAt", new Date().toString());
        mockOrders.add(0, orderPayload);
        return ResponseEntity.ok(orderPayload);
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Update order delivery or confirmation status")
    public ResponseEntity<Map<String, Object>> updateStatus(
            @PathVariable String id,
            @RequestParam String status
    ) {
        for (Map<String, Object> o : mockOrders) {
            if (o.get("id").equals(id)) {
                o.put("status", status.toUpperCase());
                return ResponseEntity.ok(o);
            }
        }
        return ResponseEntity.notFound().build();
    }

    private static Map<String, Object> createOrderMap(
            String id, String prodId, String prodTitle, String buyer,
            String phone, Double qty, String unit, Double amount, String status, String address
    ) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("productId", prodId);
        map.put("productTitle", prodTitle);
        map.put("buyerName", buyer);
        map.put("buyerPhone", phone);
        map.put("quantity", qty);
        map.put("unit", unit);
        map.put("totalAmount", amount);
        map.put("status", status);
        map.put("deliveryAddress", address);
        return map;
    }
}
