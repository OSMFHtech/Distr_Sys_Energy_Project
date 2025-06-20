package com.Distr_Sys.usage.service;

import com.Distr_Sys.usage.model.HourlyUsage;
import com.Distr_Sys.usage.model.UsageRecord;
import com.Distr_Sys.usage.model.UsageType;
import com.Distr_Sys.usage.repository.HourlyUsageRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class AggregationService {
    private final HourlyUsageRepository hourlyRepo;
    private final RabbitTemplate rabbit;

    public AggregationService(HourlyUsageRepository hourlyRepo, RabbitTemplate rabbit) {
        this.hourlyRepo = hourlyRepo;
        this.rabbit = rabbit;
    }

    public void aggregateUsage(UsageRecord record) {
        LocalDateTime usageHour = record.getTimestamp().atZone(ZoneOffset.UTC)
                .withMinute(0).withSecond(0).withNano(0).toLocalDateTime();

        HourlyUsage usage = hourlyRepo.findByUsageHour(usageHour).orElse(new HourlyUsage(usageHour));

        if (record.getType() == UsageType.USER) {
            usage.setCommunityUsed(usage.getCommunityUsed() + record.getUsedKw());
        } else if (record.getType() == UsageType.PRODUCER) {
            usage.setCommunityProduced(usage.getCommunityProduced() + record.getUsedKw());
        }

        // Simulate grid fallback
        if (usage.getCommunityProduced() < usage.getCommunityUsed()) {
            int diff = usage.getCommunityUsed() - usage.getCommunityProduced();
            usage.setGridUsed(diff);
        } else {
            usage.setGridUsed(0);
        }

        hourlyRepo.save(usage);

        rabbit.convertAndSend("energy-exchange", "hourly.update", usage);
    }
}