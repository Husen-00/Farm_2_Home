package com.farmerconnect.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication & User Management", description = "Farmer and Buyer registration, OTP login, and JWT access tokens")
@CrossOrigin(origins = "*")
public class AuthController {

    @PostMapping("/send-otp")
    @Operation(summary = "Send OTP to mobile number")
    public ResponseEntity<Map<String, Object>> sendOtp(@RequestParam String phone) {
        Map<String, Object> resp = new HashMap<>();
        resp.put("phone", phone);
        resp.put("status", "OTP_SENT");
        resp.put("message", "A 6-digit verification code has been sent to " + phone);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user with phone and OTP or password")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> credentials) {
        String phone = credentials.getOrDefault("phone", "+91 98451 23456");
        String role = credentials.getOrDefault("role", "FARMER");

        Map<String, Object> resp = new HashMap<>();
        resp.put("token", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJmYXJtZXJfMDEiLCJyb2xlIjoiRkFSTUVSIn0.mock_jwt_token");
        resp.put("tokenType", "Bearer");
        resp.put("userId", "farmer_01");
        resp.put("name", "Basavaraj Patil");
        resp.put("phone", phone);
        resp.put("role", role);
        resp.put("village", "Srirangapatna");
        resp.put("district", "Mandya");
        resp.put("state", "Karnataka");

        return ResponseEntity.ok(resp);
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new Farmer or Buyer profile")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, Object> userData) {
        userData.put("id", "user_" + UUID.randomUUID().toString().substring(0, 8));
        userData.put("status", "ACTIVE");
        return ResponseEntity.ok(userData);
    }
}
