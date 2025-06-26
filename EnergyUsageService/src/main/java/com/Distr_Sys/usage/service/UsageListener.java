package com.Distr_Sys.usage.service;

import com.Distr_Sys.usage.config.RabbitConfig;
import com.Distr_Sys.usage.model.UsageRecord;
import com.Distr_Sys.usage.model.UpdateMessage;
import com.Distr_Sys.usage.repository.UsageRecordRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class UsageListener {
    private final UsageRecordRepository usageRecordRepository;
    private final RabbitTemplate rabbitTemplate;

    public UsageListener(UsageRecordRepository usageRecordRepository, RabbitTemplate rabbitTemplate) {
        this.usageRecordRepository = usageRecordRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    // Message format: {"type":"USER"|"PRODUCER", "userId":..., "datetime":"...", "kwh":...}
    @RabbitListener(queues = RabbitConfig.QUEUE)
    @Transactional
    public void handleMessage(Map<String, Object> message) {
        String type = (String) message.get("type");
        LocalDateTime datetime = LocalDateTime.parse((String) message.get("datetime"));
        double kwh = ((Number) message.get("kwh")).doubleValue();

        LocalDateTime hour = datetime.withMinute(0).withSecond(0).withNano(0);
        UsageRecord record = usageRecordRepository.findByHour(hour)
                .orElse(new UsageRecord(hour, 0, 0, 0));

        if ("PRODUCER".equals(type)) {
            record.setCommunityProduced(record.getCommunityProduced() + kwh);
        } else if ("USER".equals(type)) {
            double available = record.getCommunityProduced() - record.getCommunityUsed();
            double fromCommunity = Math.min(kwh, Math.max(available, 0));
            double fromGrid = kwh - fromCommunity;
            record.setCommunityUsed(record.getCommunityUsed() + fromCommunity);
            record.setGridUsed(record.getGridUsed() + fromGrid);
        }

        usageRecordRepository.save(record);

        UpdateMessage update = new UpdateMessage();
        update.setHour(hour.toString());
        update.setCommunityProduced(record.getCommunityProduced());
        update.setCommunityUsed(record.getCommunityUsed());
        update.setGridUsed(record.getGridUsed());

        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE, "usage.update", update);
    }
}