package com.Distr_Sys.user.service;

import com.Distr_Sys.user.model.EnergyMessage;
import com.Distr_Sys.user.config.RabbitConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class UserMessageSender {
    private final RabbitTemplate rabbitTemplate;
    private final Random random = new Random();

    public UserMessageSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Scheduled(fixedRate = 5000)
    public void sendUserConsumption() {
        long userId = 1L;
        LocalDateTime now = LocalDateTime.now();
        double kwh = generatePlausibleKwh(now);
        EnergyMessage msg = new EnergyMessage(EnergyMessage.Type.USER, userId, now, kwh);
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.ROUTING_KEY, msg);
        System.out.printf("[User] Sent: %.3f kWh at %s%n", kwh, now);
    }

    private double generatePlausibleKwh(LocalDateTime now) {
        int hour = now.getHour();
        double basePerHour;
        if (hour >= 6 && hour < 9) basePerHour = 12.0 + random.nextDouble() * 3.0; // 12-15 kWh/h
        else if (hour >= 17 && hour < 22) basePerHour = 13.0 + random.nextDouble() * 3.0; // 13-16 kWh/h
        else if (hour >= 0 && hour < 6) basePerHour = 3.0 + random.nextDouble() * 1.0; // 3-4 kWh/h
        else basePerHour = 9.0 + random.nextDouble() * 3.0; // 9-12 kWh/h
        double basePer5Sec = basePerHour / 720.0;
        double kwh = basePer5Sec + random.nextDouble() * basePer5Sec * 0.5;
        if (random.nextDouble() < 0.15) kwh *= 1.2;
        return Math.round(kwh * 1000.0) / 1000.0;
    }
}