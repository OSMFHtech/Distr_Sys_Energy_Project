package com.Distr_Sys.usage.messaging;

import com.Distr_Sys.common.EnergyMessage;
import com.Distr_Sys.usage.model.UsageRecord;
import com.Distr_Sys.usage.model.UsageType;
import com.Distr_Sys.usage.repository.UsageRepository;
import com.Distr_Sys.usage.service.AggregationService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class UsageMessageListener {
    private final AggregationService aggregationService;
    private final UsageRepository usageRepository;

    public UsageMessageListener(AggregationService aggregationService, UsageRepository usageRepository) {
        this.aggregationService = aggregationService;
        this.usageRepository = usageRepository;
    }

    @RabbitListener(queues = "#{T(com.Distr_Sys.usage.config.RabbitConfig).QUEUE}")
    public void handleUsageMessage(EnergyMessage msg) {
        UsageRecord record = new UsageRecord();
        record.setUserId(msg.getUserId());
        record.setTimestamp(msg.getDatetime() != null ? msg.getDatetime() : Instant.now());

        if (msg.getType() == EnergyMessage.Type.PRODUCER) {
            record.setProducedKw(msg.getKwh());
            record.setUsedKw(null);
            record.setType(UsageType.PRODUCER);
        } else {
            record.setUsedKw((int) msg.getKwh());
            record.setProducedKw(null);
            record.setType(UsageType.USER);
        }

        usageRepository.save(record);
        aggregationService.aggregateUsage(record);
    }
}