package com.example.delivery.service;

import com.example.delivery.entity.Weather;
import com.example.delivery.repository.WeatherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherImportService {

    private static final String WEATHER_URL =
            "https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php";

    private static final Set<String> TRACKED_STATIONS = Set.of(
            "Tallinn-Harku",
            "Tartu-Tõravere",
            "Pärnu"
    );

    private final WeatherRepository weatherRepository;

    @Scheduled(cron = "${weather.import.cron}")
    public void importWeatherData() {
        log.info("Starting weather data import at {}", LocalDateTime.now());
        try {
            List<Weather> observations = fetchAndParse();
            weatherRepository.saveAll(observations);
            log.info("Successfully imported {} weather observations", observations.size());
        } catch (Exception e) {
            log.error("Failed to import weather data: {}", e.getMessage(), e);
        }
    }

    private List<Weather> fetchAndParse() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        try (InputStream stream = new URL(WEATHER_URL).openStream()) {
            Document doc = builder.parse(stream);
            doc.getDocumentElement().normalize();

            NodeList stations = doc.getElementsByTagName("station");
            LocalDateTime timestamp = LocalDateTime.now();

            return java.util.stream.IntStream.range(0, stations.getLength())
                    .mapToObj(i -> (Element) stations.item(i))
                    .filter(this::isTrackedStation)
                    .map(el -> parseStation(el, timestamp))
                    .toList();
        }
    }

    private boolean isTrackedStation(Element station) {
        String name = getTextContent(station, "name");
        return TRACKED_STATIONS.contains(name);
    }

    private Weather parseStation(Element station, LocalDateTime timestamp) {
        Weather weather = new Weather();
        weather.setStationName(getTextContent(station, "name"));
        weather.setWmoCode(getTextContent(station, "wmocode"));
        weather.setAirTemperature(parseDouble(getTextContent(station, "airtemperature")));
        weather.setWindSpeed(parseDouble(getTextContent(station, "windspeed")));
        weather.setWeatherPhenomenon(getTextContent(station, "phenomenon"));
        weather.setObservationTimestamp(timestamp);
        return weather;
    }

    private String getTextContent(Element element, String tagName) {
        NodeList nodes = element.getElementsByTagName(tagName);
        if (nodes.getLength() == 0) return null;
        String value = nodes.item(0).getTextContent().trim();
        return value.isBlank() ? null : value;
    }

    private Double parseDouble(String value) {
        if (value == null) return null;
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @EventListener(ApplicationReadyEvent.class)
    public void importOnStartup() {
        log.info("Triggering initial weather import on startup...");
        importWeatherData();
    }
}