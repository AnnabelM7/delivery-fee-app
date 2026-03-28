package com.example.delivery.repository;

import com.example.delivery.entity.Weather;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface WeatherRepository extends JpaRepository<Weather, UUID> {
    Optional<Weather> findFirstByStationNameOrderByObservationTimestampDesc(String stationName);

    Optional<Weather> findTopByStationNameAndObservationTimestampLessThanEqualOrderByObservationTimestampDesc(String stationName, LocalDateTime time);
}
