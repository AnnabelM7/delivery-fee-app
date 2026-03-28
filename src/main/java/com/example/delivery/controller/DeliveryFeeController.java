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

    /**
     * Calculates the delivery fee for a given city and vehicle type.
     * If datetime is provided, calculation is based on weather data valid at that time.
     *
     * @param city        the delivery city (TALLINN, TARTU, PARNU)
     * @param vehicleType the vehicle type (CAR, SCOOTER, BIKE)
     * @param datetime    optional timestamp for historical fee calculation (format: yyyy-MM-ddTHH:mm:ss)
     * @return total delivery fee in euros, or error message if vehicle type is forbidden
     */
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
