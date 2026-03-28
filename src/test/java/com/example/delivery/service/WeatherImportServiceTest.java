package com.example.delivery.service;

import com.example.delivery.entity.Weather;
import com.example.delivery.repository.WeatherRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WeatherImportServiceTest {

    @Mock
    private WeatherRepository weatherRepository;

    @InjectMocks
    private WeatherImportService weatherImportService;

    @Test
    void importWeatherData_savesThreeStations() {
        weatherImportService.importWeatherData();

        ArgumentCaptor<List<Weather>> captor = ArgumentCaptor.forClass(List.class);
        verify(weatherRepository, times(1)).saveAll(captor.capture());

        List<Weather> saved = captor.getValue();
        assertThat(saved).hasSize(3);
    }

    @Test
    void importWeatherData_stationNamesAreCorrect() {
        weatherImportService.importWeatherData();

        ArgumentCaptor<List<Weather>> captor = ArgumentCaptor.forClass(List.class);
        verify(weatherRepository).saveAll(captor.capture());

        List<String> stationNames = captor.getValue().stream()
                .map(Weather::getStationName)
                .toList();

        assertThat(stationNames).containsExactlyInAnyOrder(
                "Tallinn-Harku",
                "Tartu-Tõravere",
                "Pärnu"
        );
    }

    @Test
    void importWeatherData_onFailure_doesNotThrow() {
        when(weatherRepository.saveAll(any())).thenThrow(new RuntimeException("DB error"));

        // ei tohiks visata exception'it
        weatherImportService.importWeatherData();
    }
}