package com.example.delivery.service;

import com.example.delivery.entity.Weather;
import com.example.delivery.exception.ResourceNotFoundException;
import com.example.delivery.repository.WeatherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private final WeatherRepository weatherRepository;

    /**
     * Returns all weather observations.
     */
    public List<Weather> getAllWeather() {
        return weatherRepository.findAll();
    }

    /**
     * Returns a weather observation by ID.
     *
     * @param id the observation ID
     * @return the matching Weather entry
     * @throws ResourceNotFoundException if no observation found with given ID
     */
    public Weather getWeatherById(UUID id) {
        return weatherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Weather not found with id=" + id));
    }


    /**
     * Returns the latest weather observation for a given station.
     *
     * @param stationName the name of the weather station
     * @return the most recent Weather entry
     * @throws ResourceNotFoundException if no data exists for the station
     */
    public Weather getLatestWeather(String stationName) {
        return weatherRepository.findFirstByStationNameOrderByObservationTimestampDesc(stationName)
                .orElseThrow(() -> new ResourceNotFoundException("No weather data found for station: " + stationName));
    }

    /**
     * Returns the latest weather observation for a given station at or before the specified time.
     *
     * @param stationName the name of the weather station
     * @param time        the upper bound timestamp
     * @return the most recent Weather entry at or before the given time
     * @throws ResourceNotFoundException if no data exists for the station at the given time
     */
    public Weather getWeatherForTime(String stationName, LocalDateTime time) {
        return weatherRepository
                .findTopByStationNameAndObservationTimestampLessThanEqualOrderByObservationTimestampDesc(
                        stationName, time)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No weather data found for station " + stationName + " at " + time
                ));
    }
}