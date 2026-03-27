package com.example.delivery.controller;

import com.example.delivery.entity.Weather;
import com.example.delivery.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping
    public ResponseEntity<List<Weather>> getAll() {
        return ResponseEntity.ok(weatherService.getAllWeather());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Weather> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(weatherService.getWeatherById(id));
    }
}