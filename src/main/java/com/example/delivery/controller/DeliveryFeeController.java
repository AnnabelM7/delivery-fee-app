package com.example.delivery.controller;

import com.example.delivery.enums.City;
import com.example.delivery.enums.VehicleType;
import com.example.delivery.service.DeliveryFeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/delivery-fee")
@RequiredArgsConstructor
public class DeliveryFeeController {
    private final DeliveryFeeService deliveryFeeService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getDeliveryFee(
            @RequestParam City city,
            @RequestParam VehicleType vehicleType) {

        double fee = deliveryFeeService.calculateFee(city, vehicleType);
        return ResponseEntity.ok(Map.of(
                "city", city,
                "vehicleType", vehicleType,
                "fee", fee
        ));
    }
}
