package com.Distr_Sys.producer.service;

import com.Distr_Sys.producer.model.EnergyMessage;
import com.Distr_Sys.producer.config.RabbitConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class ProducerService {
    private final RabbitTemplate rabbit;
    private final Random random = new Random();

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

    private double generateKwhForCurrentTime() {
        int hour = LocalDateTime.now().getHour();
        // Simulate more production during midday
        if (hour >= 10 && hour <= 16) {
            return 0.004 + random.nextDouble() * 0.002;
        } else {
            return 0.001 + random.nextDouble() * 0.001;
        }
    }
}