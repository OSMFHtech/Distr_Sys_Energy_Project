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
        double base;
        if (hour >= 6 && hour < 9) base = 2.0;      // Morning peak
        else if (hour >= 17 && hour < 22) base = 2.5; // Evening peak
        else if (hour >= 0 && hour < 6) base = 0.3;   // Night
        else base = 1.0;                              // Day
        return base + random.nextDouble() * base * 0.5;
    }
}