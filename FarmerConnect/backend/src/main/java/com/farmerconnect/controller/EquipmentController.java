package com.farmerconnect.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/equipment")
@Tag(name = "Equipment Rental & CHC", description = "Endpoints for farm mechanization, tractor, and harvester rentals")
@CrossOrigin(origins = "*")
public class EquipmentController {

    private final List<Map<String, Object>> equipmentList = Arrays.asList(
            createEquipmentMap("EQ-01", "Mahindra 575 DI Tractor (45 HP)", "ಮಹೀಂದ್ರಾ 575 ಡಿಐ ಟ್ರ್ಯಾಕ್ಟರ್", "Rudrappa", "+91 94481 00112", 450.0, 3200.0, "Mandya (3 km away)", 4.9),
            createEquipmentMap("EQ-02", "Paddy Combine Harvester", "ಭತ್ತ ಕೊಯ್ಲು ಮಾಡುವ ಯಂತ್ರ", "Veeranna Gowda", "+91 98450 33445", 1600.0, 12000.0, "Maddur (6 km away)", 4.8),
            createEquipmentMap("EQ-03", "Power Tiller & Rotary Weeder", "ಪವರ್ ಟಿಲ್ಲರ್ ಮತ್ತು ಕಳೆ ಕತ್ತರಿಸುವ ಯಂತ್ರ", "Nagappa", "+91 97311 55667", 280.0, 1800.0, "Srirangapatna (2 km away)", 4.7),
            createEquipmentMap("EQ-04", "Tractor-Mounted Boom Sprayer", "ಟ್ರ್ಯಾಕ್ಟರ್ ಮೌಂಟೆಡ್ ಬೂಮ್ ಸ್ಪ್ರೇಯರ್", "Somasekhar", "+91 99002 44331", 350.0, 2400.0, "Pandavapura (8 km away)", 4.8)
    );

    private final List<Map<String, Object>> bookings = new ArrayList<>();

    @GetMapping
    @Operation(summary = "List nearby farm machinery and custom hiring equipment")
    public ResponseEntity<List<Map<String, Object>>> getEquipment() {
        return ResponseEntity.ok(equipmentList);
    }

    @PostMapping("/book")
    @Operation(summary = "Book equipment for hourly or daily rental")
    public ResponseEntity<Map<String, Object>> bookEquipment(@RequestBody Map<String, Object> bookingPayload) {
        String id = "EQP-" + (System.currentTimeMillis() % 10000);
        bookingPayload.put("id", id);
        bookingPayload.put("status", "CONFIRMED");
        bookings.add(0, bookingPayload);
        return ResponseEntity.ok(bookingPayload);
    }

    private static Map<String, Object> createEquipmentMap(
            String id, String nameEn, String nameKn, String owner,
            String phone, Double hourly, Double daily, String location, Double rating
    ) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("nameEn", nameEn);
        map.put("nameKn", nameKn);
        map.put("ownerName", owner);
        map.put("ownerPhone", phone);
        map.put("hourlyRate", hourly);
        map.put("dailyRate", daily);
        map.put("location", location);
        map.put("rating", rating);
        map.put("isAvailable", true);
        return map;
    }
}
