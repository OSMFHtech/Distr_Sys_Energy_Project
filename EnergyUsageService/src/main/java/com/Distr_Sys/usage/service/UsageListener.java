package com.Distr_Sys.usage.service;

import com.Distr_Sys.usage.config.RabbitConfig;
import com.Distr_Sys.usage.model.UsageRecord;
import com.Distr_Sys.usage.model.UpdateMessage;
import com.Distr_Sys.usage.model.EnergyMessage;
import com.Distr_Sys.usage.repository.UsageRecordRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class UsageListener {
    private final UsageRecordRepository usageRecordRepository;
    private final RabbitTemplate rabbitTemplate;

    public UsageListener(UsageRecordRepository usageRecordRepository, RabbitTemplate rabbitTemplate) {
        this.usageRecordRepository = usageRecordRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = RabbitConfig.QUEUE)
    @Transactional
    public void handleMessage(EnergyMessage message) {
        LocalDateTime datetime = message.getDatetime();
        if (datetime == null) return;

        String type = message.getType().name();
        double kwh = message.getKwh();

        LocalDateTime hour = datetime.withMinute(0).withSecond(0).withNano(0);
        UsageRecord record = usageRecordRepository.findByHour(hour)
                .orElse(new UsageRecord(hour, 0, 0, 0));

        if ("PRODUCER".equals(type)) {
            // Multiply by 10 for produced
            record.setCommunityProduced(record.getCommunityProduced() + kwh * 10);
        } else if ("USER".equals(type)) {
            // Multiply by 10 for used
            double available = record.getCommunityProduced() - record.getCommunityUsed();
            double fromCommunity = Math.min(kwh * 10, Math.max(available, 0));
            double fromGrid = kwh - (fromCommunity / 10.0); // gridUsed remains in original kWh

            record.setCommunityUsed(record.getCommunityUsed() + fromCommunity);
            record.setGridUsed(record.getGridUsed() + fromGrid);
        }

        // Round all to 3 decimals
        record.setCommunityProduced(Math.round(record.getCommunityProduced() * 1000.0) / 1000.0);
        record.setCommunityUsed(Math.round(record.getCommunityUsed() * 1000.0) / 1000.0);
        record.setGridUsed(Math.round(record.getGridUsed() * 1000.0) / 1000.0);

        usageRecordRepository.save(record);
        // Create and send update message
        UpdateMessage update = new UpdateMessage();
        update.setHour(hour.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        update.setCommunityProduced(record.getCommunityProduced());
        update.setCommunityUsed(record.getCommunityUsed());
        update.setGridUsed(record.getGridUsed());
        update.setGridUsedDisplay(record.getGridUsed() / 100.0); // for display

        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE, "usage.update", update);
    }
}