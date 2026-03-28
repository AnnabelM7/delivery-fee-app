package com.example.delivery.service;

import com.example.delivery.entity.Weather;
import com.example.delivery.exception.ResourceNotFoundException;
import com.example.delivery.repository.WeatherRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class WeatherService {

    private final WeatherRepository weatherRepository;

    public WeatherService(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }

    public List<Weather> getAllWeather() {
        return weatherRepository.findAll();
    }

    public Weather getWeatherById(UUID id) {
        return weatherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Weather not found with id=" + id));
    }

    public Weather getLatestWeather(String stationName) {
        return weatherRepository.findFirstByStationNameOrderByObservationTimestampDesc(stationName)
                .orElseThrow(() -> new ResourceNotFoundException("No weather data found for station: " + stationName));
    }

    public Weather getWeatherForTime(String stationName, LocalDateTime time) {
        return weatherRepository
                .findTopByStationNameAndObservationTimestampLessThanEqualOrderByObservationTimestampDesc(
                        stationName, time)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No weather data found for station " + stationName + " at " + time
                ));
    }
}