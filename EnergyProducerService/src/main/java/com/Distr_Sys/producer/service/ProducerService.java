package com.Distr_Sys.producer.service;

import com.Distr_Sys.producer.model.EnergyMessage;
import com.Distr_Sys.producer.config.RabbitConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class ProducerService {
    private final RabbitTemplate rabbit;
    private final Random random = new Random();

    @Value("${weather.api.key:dummy}")
    private String weatherApiKey;
    @Value("${weather.api.lat:48.2}")
    private String lat;
    @Value("${weather.api.lon:16.3}")
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
        try {
            String url = String.format(
                    "https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=%s",
                    lat, lon, weatherApiKey
            );
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(url, String.class);
            JSONObject json = new JSONObject(response);
            int cloudiness = json.getJSONObject("clouds").getInt("all"); // 0-100
            double sunlight = 1.0 - (cloudiness / 100.0); // 1.0 = clear, 0.0 = fully cloudy

            int hour = LocalDateTime.now().getHour();
            double timeFactor = (hour >= 7 && hour <= 18)
                    ? 0.5 + 0.5 * Math.sin(Math.PI * (hour - 7) / 11)
                    : 0.05;
            return sunlight * timeFactor;
        } catch (Exception e) {
            // fallback to original logic
            int hour = LocalDateTime.now().getHour();
            if (hour >= 7 && hour <= 18) {
                double peak = Math.sin(Math.PI * (hour - 7) / 11);
                return 0.5 + 0.5 * peak;
            }
            return 0.05;
        }
    }

    private double generateKwhForCurrentTime() {
        int hour = LocalDateTime.now().getHour();
        double sunlight = getSunlightFactor();
        double basePerHour;
        if (hour >= 10 && hour <= 16) {
            basePerHour = 8.0 + random.nextDouble() * 3.0; // 8-11 kWh/h at peak
        } else if (hour >= 7 && hour < 10 || hour > 16 && hour <= 18) {
            basePerHour = 3.0 + random.nextDouble() * 2.0; // 3-5 kWh/h
        } else {
            basePerHour = 0.2 + random.nextDouble() * 0.3; // 0.2-0.5 kWh/h at night
        }
        double basePer5Sec = basePerHour / 720.0;
        double kwh = basePer5Sec * sunlight;
        return Math.round(kwh * 1000.0) / 1000.0;
    }
}