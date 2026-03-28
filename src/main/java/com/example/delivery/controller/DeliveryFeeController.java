package com.example.delivery.controller;

import com.example.delivery.enums.City;
import com.example.delivery.enums.VehicleType;
import com.example.delivery.service.DeliveryFeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/delivery-fee")
@RequiredArgsConstructor
public class DeliveryFeeController {
    private final DeliveryFeeService deliveryFeeService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getDeliveryFee(
            @RequestParam City city,
            @RequestParam VehicleType vehicleType,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime datetime) {

        double fee = deliveryFeeService.calculateFee(city, vehicleType, datetime);

        Map<String, Object> response = new HashMap<>();
        response.put("city", city);
        response.put("vehicleType", vehicleType);
        response.put("fee", fee);

        if (datetime != null) {
            response.put("datetime", datetime);
        }

        return ResponseEntity.ok(response);
    }
}
