package com.example.delivery.repository;

import com.example.delivery.entity.Weather;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface WeatherRepository extends JpaRepository<Weather, UUID> {
    /**
     * Returns the most recent weather observation for a given station.
     *
     * @param stationName the name of the weather station
     * @return the latest Weather entry, or empty if none found
     */
    Optional<Weather> findFirstByStationNameOrderByObservationTimestampDesc(String stationName);

    /**
     * Returns the most recent weather observation for a given station
     * at or before the specified timestamp.
     *
     * @param stationName the name of the weather station
     * @param time        the upper bound timestamp
     * @return the latest Weather entry at or before the given time, or empty if none found
     */
    Optional<Weather> findTopByStationNameAndObservationTimestampLessThanEqualOrderByObservationTimestampDesc(String stationName, LocalDateTime time);
}
