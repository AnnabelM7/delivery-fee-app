package com.example.delivery.service;

import com.example.delivery.entity.BaseFee;
import com.example.delivery.entity.Weather;
import com.example.delivery.enums.City;
import com.example.delivery.enums.VehicleType;
import com.example.delivery.exception.ForbiddenVehicleException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class DeliveryFeeServiceTest {

    @Mock
    private WeatherService weatherService;

    @Mock
    private BaseFeeService baseFeeService;

    @Mock
    private ExtraFeeService extraFeeService;

    @InjectMocks
    private DeliveryFeeService deliveryFeeService;

    private BaseFee tartuBikeFee;
    private Weather clearWarmWeather;

    @BeforeEach
    void setUp() {
        tartuBikeFee = new BaseFee();
        tartuBikeFee.setCity(City.TARTU);
        tartuBikeFee.setVehicleType(VehicleType.BIKE);
        tartuBikeFee.setFee(2.5);

        clearWarmWeather = new Weather();
        clearWarmWeather.setStationName("Tartu-Tõravere");
        clearWarmWeather.setAirTemperature(5.0);
        clearWarmWeather.setWindSpeed(3.0);
        clearWarmWeather.setWeatherPhenomenon("Clear");

        when(extraFeeService.calculateAirTemperatureExtraFee(any(), any())).thenReturn(0.0);
        when(extraFeeService.calculateWindSpeedExtraFee(any(), any())).thenReturn(0.0);
        when(extraFeeService.calculateWeatherPhenomenonExtraFee(any(), any())).thenReturn(0.0);
    }

    @Test
    void calculateFee_noExtraFees_returnsBaseFee() {
        when(baseFeeService.getBaseFeeByCityAndVehicleType(City.TARTU, VehicleType.BIKE)).thenReturn(tartuBikeFee);
        when(weatherService.getLatestWeather("Tartu-Tõravere")).thenReturn(clearWarmWeather);

        double fee = deliveryFeeService.calculateFee(City.TARTU, VehicleType.BIKE, null);

        assertThat(fee).isEqualTo(2.5);
    }

    @Test
    void calculateFee_coldTemperature_addsAtef() {
        when(baseFeeService.getBaseFeeByCityAndVehicleType(City.TARTU, VehicleType.BIKE)).thenReturn(tartuBikeFee);
        when(weatherService.getLatestWeather("Tartu-Tõravere")).thenReturn(clearWarmWeather);
        when(extraFeeService.calculateAirTemperatureExtraFee(any(), any())).thenReturn(0.5);

        double fee = deliveryFeeService.calculateFee(City.TARTU, VehicleType.BIKE, null);

        assertThat(fee).isEqualTo(3.0);
    }

    @Test
    void calculateFee_veryColTemperature_addsHigherAtef() {
        when(baseFeeService.getBaseFeeByCityAndVehicleType(City.TARTU, VehicleType.BIKE)).thenReturn(tartuBikeFee);
        when(weatherService.getLatestWeather("Tartu-Tõravere")).thenReturn(clearWarmWeather);
        when(extraFeeService.calculateAirTemperatureExtraFee(any(), any())).thenReturn(1.0);

        double fee = deliveryFeeService.calculateFee(City.TARTU, VehicleType.BIKE, null);

        assertThat(fee).isEqualTo(3.5);
    }

    @Test
    void calculateFee_highWindSpeed_addsWsef() {
        when(baseFeeService.getBaseFeeByCityAndVehicleType(City.TARTU, VehicleType.BIKE)).thenReturn(tartuBikeFee);
        when(weatherService.getLatestWeather("Tartu-Tõravere")).thenReturn(clearWarmWeather);
        when(extraFeeService.calculateWindSpeedExtraFee(any(), any())).thenReturn(0.5);

        double fee = deliveryFeeService.calculateFee(City.TARTU, VehicleType.BIKE, null);

        assertThat(fee).isEqualTo(3.0);
    }

    @Test
    void calculateFee_windSpeedOver20_throwsForbidden() {
        when(baseFeeService.getBaseFeeByCityAndVehicleType(City.TARTU, VehicleType.BIKE)).thenReturn(tartuBikeFee);
        when(weatherService.getLatestWeather("Tartu-Tõravere")).thenReturn(clearWarmWeather);
        when(extraFeeService.calculateWindSpeedExtraFee(any(), any())).thenThrow(new ForbiddenVehicleException());

        assertThatThrownBy(() -> deliveryFeeService.calculateFee(City.TARTU, VehicleType.BIKE, null))
                .isInstanceOf(ForbiddenVehicleException.class);
    }

    @Test
    void calculateFee_snowPhenomenon_addsWpef() {
        when(baseFeeService.getBaseFeeByCityAndVehicleType(City.TARTU, VehicleType.BIKE)).thenReturn(tartuBikeFee);
        when(weatherService.getLatestWeather("Tartu-Tõravere")).thenReturn(clearWarmWeather);
        when(extraFeeService.calculateWeatherPhenomenonExtraFee(any(), any())).thenReturn(1.0);

        double fee = deliveryFeeService.calculateFee(City.TARTU, VehicleType.BIKE, null);

        assertThat(fee).isEqualTo(3.5);
    }

    @Test
    void calculateFee_rainPhenomenon_addsWpef() {
        when(baseFeeService.getBaseFeeByCityAndVehicleType(City.TARTU, VehicleType.BIKE)).thenReturn(tartuBikeFee);
        when(weatherService.getLatestWeather("Tartu-Tõravere")).thenReturn(clearWarmWeather);
        when(extraFeeService.calculateWeatherPhenomenonExtraFee(any(), any())).thenReturn(0.5);

        double fee = deliveryFeeService.calculateFee(City.TARTU, VehicleType.BIKE, null);

        assertThat(fee).isEqualTo(3.0);
    }

    @Test
    void calculateFee_thunderPhenomenon_throwsForbidden() {
        when(baseFeeService.getBaseFeeByCityAndVehicleType(City.TARTU, VehicleType.BIKE)).thenReturn(tartuBikeFee);
        when(weatherService.getLatestWeather("Tartu-Tõravere")).thenReturn(clearWarmWeather);
        when(extraFeeService.calculateWeatherPhenomenonExtraFee(any(), any())).thenThrow(new ForbiddenVehicleException());

        assertThatThrownBy(() -> deliveryFeeService.calculateFee(City.TARTU, VehicleType.BIKE, null))
                .isInstanceOf(ForbiddenVehicleException.class);
    }

    @Test
    void calculateFee_carInBadWeather_noExtraFees() {
        BaseFee tallinnCarFee = new BaseFee();
        tallinnCarFee.setCity(City.TALLINN);
        tallinnCarFee.setVehicleType(VehicleType.CAR);
        tallinnCarFee.setFee(4.0);

        when(baseFeeService.getBaseFeeByCityAndVehicleType(City.TALLINN, VehicleType.CAR)).thenReturn(tallinnCarFee);
        when(weatherService.getLatestWeather("Tallinn-Harku")).thenReturn(clearWarmWeather);

        double fee = deliveryFeeService.calculateFee(City.TALLINN, VehicleType.CAR, null);

        assertThat(fee).isEqualTo(4.0);
    }

    @Test
    void calculateFee_taskExample_returnsCorrectFee() {
        // TARTU + BIKE, temp=-2.1, wind=4.7, phenomenon="Light snow shower" = 4.0
        Weather tartuWeather = new Weather();
        tartuWeather.setStationName("Tartu-Tõravere");
        tartuWeather.setAirTemperature(-2.1);
        tartuWeather.setWindSpeed(4.7);
        tartuWeather.setWeatherPhenomenon("Light snow shower");

        when(baseFeeService.getBaseFeeByCityAndVehicleType(City.TARTU, VehicleType.BIKE)).thenReturn(tartuBikeFee);
        when(weatherService.getLatestWeather("Tartu-Tõravere")).thenReturn(tartuWeather);
        when(extraFeeService.calculateAirTemperatureExtraFee(any(), any())).thenReturn(0.5);
        when(extraFeeService.calculateWeatherPhenomenonExtraFee(any(), any())).thenReturn(1.0);

        double fee = deliveryFeeService.calculateFee(City.TARTU, VehicleType.BIKE, null);

        assertThat(fee).isEqualTo(4.0);
    }
}