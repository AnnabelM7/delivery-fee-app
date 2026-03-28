package com.example.delivery.service;

import com.example.delivery.entity.BaseFee;
import com.example.delivery.entity.Weather;
import com.example.delivery.enums.City;
import com.example.delivery.enums.VehicleType;
import com.example.delivery.exception.ForbiddenVehicleException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class DeliveryFeeService {

    private static final Map<City, String> CITY_TO_STATION = Map.of(
            City.TALLINN, "Tallinn-Harku",
            City.TARTU, "Tartu-Tõravere",
            City.PARNU, "Pärnu"
    );

    private final WeatherService weatherService;
    private final BaseFeeService baseFeeService;

    public DeliveryFeeService(WeatherService weatherService, BaseFeeService baseFeeService) {
        this.weatherService = weatherService;
        this.baseFeeService = baseFeeService;
    }

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
        fee += getAirTemperatureExtraFee(weather.getAirTemperature(), vehicleType);
        fee += getWindSpeedExtraFee(weather.getWindSpeed(), vehicleType);
        fee += getWeatherPhenomenonExtraFee(weather.getWeatherPhenomenon(), vehicleType);

        return fee;
    }

    private double getAirTemperatureExtraFee(Double temperature, VehicleType vehicleType) {
        if (temperature == null) return 0.0;
        if (vehicleType != VehicleType.SCOOTER && vehicleType != VehicleType.BIKE) return 0.0;

        if (temperature < -10.0) return 1.0;
        if (temperature <= 0.0) return 0.5;
        return 0.0;
    }

    private double getWindSpeedExtraFee(Double windSpeed, VehicleType vehicleType) {
        if (windSpeed == null) return 0.0;
        if (vehicleType != VehicleType.BIKE) return 0.0;

        if (windSpeed > 20.0) throw new ForbiddenVehicleException();
        if (windSpeed >= 10.0) return 0.5;
        return 0.0;
    }

    private double getWeatherPhenomenonExtraFee(String phenomenon, VehicleType vehicleType) {
        if (phenomenon == null || phenomenon.isBlank()) return 0.0;
        if (vehicleType != VehicleType.SCOOTER && vehicleType != VehicleType.BIKE) return 0.0;

        String p = phenomenon.toLowerCase();

        if (p.contains("glaze") || p.contains("hail") || p.contains("thunder")) {
            throw new ForbiddenVehicleException();
        }
        if (p.contains("snow") || p.contains("sleet")) return 1.0;
        if (p.contains("rain")) return 0.5;

        return 0.0;
    }
}