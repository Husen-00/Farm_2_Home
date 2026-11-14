package com.farmerconnect.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/labor")
@Tag(name = "Agricultural Labour", description = "Endpoints for hiring transplanting, weeding, and harvesting labour teams")
@CrossOrigin(origins = "*")
public class LaborController {

    private final List<Map<String, Object>> laborListings = new ArrayList<>(Arrays.asList(
            createLaborMap("LAB-01", "Chikkanna Gowda Team", "+91 98440 12890", "Srirangapatna", "Paddy Transplanting, Weeding & Harvesting", 8, 450.0),
            createLaborMap("LAB-02", "Maruthi Sugarcane Cutters", "+91 99011 44556", "Maddur", "Sugarcane Harvesting, Binding & Truck Loading", 12, 500.0)
    ));

    private final List<Map<String, Object>> laborBookings = new ArrayList<>();

    @GetMapping
    @Operation(summary = "Get available labour teams in district")
    public ResponseEntity<List<Map<String, Object>>> getLaborListings() {
        return ResponseEntity.ok(laborListings);
    }

    @PostMapping("/book")
    @Operation(summary = "Book a labour team for farm operations")
    public ResponseEntity<Map<String, Object>> bookLabor(@RequestBody Map<String, Object> bookingData) {
        String id = "LAB-BK-" + (System.currentTimeMillis() % 10000);
        bookingData.put("id", id);
        bookingData.put("status", "CONFIRMED");
        laborBookings.add(0, bookingData);
        return ResponseEntity.ok(bookingData);
    }

    private static Map<String, Object> createLaborMap(
            String id, String name, String phone, String village, String skills, int teamSize, double wage
    ) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("teamLeadName", name);
        map.put("phone", phone);
        map.put("village", village);
        map.put("skills", skills);
        map.put("teamSize", teamSize);
        map.put("dailyWagePerWorker", wage);
        map.put("isAvailable", true);
        return map;
    }
}
