package com.example.delivery.controller;

import com.example.delivery.entity.Weather;
import com.example.delivery.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    /**
     * Returns all weather observations.
     */
    @GetMapping
    public ResponseEntity<List<Weather>> getAll() {
        return ResponseEntity.ok(weatherService.getAllWeather());
    }

    /**
     * Returns a weather observation by ID.
     *
     * @param id the observation ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Weather> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(weatherService.getWeatherById(id));
    }
}