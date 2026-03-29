package com.example.delivery.service;

import com.example.delivery.entity.BaseFee;
import com.example.delivery.entity.Weather;
import com.example.delivery.enums.City;
import com.example.delivery.enums.VehicleType;
import com.example.delivery.exception.ForbiddenVehicleException;
import com.example.delivery.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DeliveryFeeService {

    private static final Map<City, String> CITY_TO_STATION = Map.of(
            City.TALLINN, "Tallinn-Harku",
            City.TARTU, "Tartu-Tõravere",
            City.PARNU, "Pärnu"
    );

    private final WeatherService weatherService;
    private final BaseFeeService baseFeeService;
    private final ExtraFeeService extraFeeService;

    /**
     * Calculates the total delivery fee for a given city and vehicle type
     * based on the latest weather data and configured base fees.
     * If a datetime is provided, uses weather data valid at that specific time.
     *
     * @param city        the delivery city
     * @param vehicleType the vehicle type used for delivery
     * @param time        optional timestamp for historical fee calculation
     * @return total delivery fee in euros
     * @throws ResourceNotFoundException if no base fee or weather data is found
     * @throws ForbiddenVehicleException if weather conditions forbid the selected vehicle type
     */
    public double calculateFee(City city, VehicleType vehicleType, LocalDateTime time) {
        String stationName = CITY_TO_STATION.get(city);

        Weather weather;

        if (time == null) {
            weather = weatherService.getLatestWeather(stationName);
        } else {
            weather = weatherService.getWeatherForTime(stationName, time);
        }
        BaseFee baseFee = baseFeeService.getBaseFeeByCityAndVehicleType(city, vehicleType);

        double fee = baseFee.getFee();
        fee += extraFeeService.calculateAirTemperatureExtraFee(weather.getAirTemperature(), vehicleType);
        fee += extraFeeService.calculateWindSpeedExtraFee(weather.getWindSpeed(), vehicleType);
        fee += extraFeeService.calculateWeatherPhenomenonExtraFee(weather.getWeatherPhenomenon(), vehicleType);

        return fee;
    }

}