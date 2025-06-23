
package com.Distr_Sys.usage.service;

import com.Distr_Sys.usage.shared.UpdateMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class HourlyUpdateProducer {
    private final RabbitTemplate rabbitTemplate;

    public HourlyUpdateProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Scheduled(cron = "0 0 * * * *") // every hour
    public void sendHourlyUpdate() {
        UpdateMessage msg = new UpdateMessage();
        msg.setHour(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        msg.setCommunityProduced(100); // replace with real aggregated value
        msg.setCommunityUsed(80);      // replace with real aggregated value
        msg.setGridUsed(20);           // replace with real aggregated value

        // Correct exchange and routing key
        rabbitTemplate.convertAndSend("energy-exchange", "usage.update", msg);
    }
}