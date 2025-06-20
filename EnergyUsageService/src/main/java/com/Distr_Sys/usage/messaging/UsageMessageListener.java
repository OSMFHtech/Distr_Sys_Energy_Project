package com.Distr_Sys.usage.messaging;

import com.Distr_Sys.usage.model.UsageRecord;
import com.Distr_Sys.usage.service.AggregationService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class UsageMessageListener {
    private final AggregationService aggregationService;

    public UsageMessageListener(AggregationService aggregationService) {
        this.aggregationService = aggregationService;
    }

    @RabbitListener(queues = "#{T(com.Distr_Sys.usage.config.RabbitConfig).QUEUE}")
    public void handleUsageMessage(UsageRecord record) {
        aggregationService.aggregateUsage(record);
    }
}