package com.Distr_Sys.producer.service;

import com.Distr_Sys.producer.model.EnergyMessage;
import com.Distr_Sys.producer.config.RabbitConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;

@Service
public class ProducerService {
    private final RabbitTemplate rabbit;
    private final Random random = new Random();

    @Value("${weather.api.key}")
    private String weatherApiKey;
    @Value("${weather.api.lat}")
    private String lat;
    @Value("${weather.api.lon}")
    private String lon;

    public ProducerService(RabbitTemplate rabbit) {
        this.rabbit = rabbit;
    }

    @Scheduled(fixedRate = 5000)
    public void sendProductionMessage() {
        double kwh = generateKwhForCurrentTime();
        EnergyMessage msg = new EnergyMessage(
                EnergyMessage.Type.PRODUCER,
                1L,
                LocalDateTime.now(),
                kwh
        );
        rabbit.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.ROUTING_KEY, msg);
        System.out.printf("[Producer] Sent: %.3f kWh at %s%n", kwh, msg.getDatetime());
    }

    private double getSunlightFactor() {
        String url = String.format(
                "https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=%s",
                lat, lon, weatherApiKey
        );
        RestTemplate restTemplate = new RestTemplate();
        try {
            Map response = restTemplate.getForObject(url, Map.class);
            Map<String, Object> clouds = (Map<String, Object>) response.get("clouds");
            int cloudiness = (int) clouds.get("all"); // 0 (clear) to 100 (overcast)
            return 1.0 - (cloudiness / 100.0); // 1.0 = full sun, 0.0 = fully cloudy
        } catch (Exception e) {
            return 0.7; // fallback if API fails
        }
    }

    private double generateKwhForCurrentTime() {
        int hour = LocalDateTime.now().getHour();
        double sunlight = getSunlightFactor();
        double base;
        if (hour >= 10 && hour <= 16) {
            base = 0.004 + random.nextDouble() * 0.002;
        } else {
            base = 0.001 + random.nextDouble() * 0.001;
        }
        return base * sunlight;
    }
}