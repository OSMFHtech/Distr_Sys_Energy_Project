package com.Distr_Sys.usage.service;

import com.Distr_Sys.usage.shared.UpdateMessage;
import com.Distr_Sys.usage.model.UsageRecord;
import com.Distr_Sys.usage.model.UsageType;
import com.Distr_Sys.usage.repository.UsageRepository;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AggregationService {
    private final UsageRepository usageRepository;
    private final AmqpTemplate amqpTemplate;

    @Autowired
    public AggregationService(UsageRepository usageRepository, AmqpTemplate amqpTemplate) {
        this.usageRepository = usageRepository;
        this.amqpTemplate = amqpTemplate;
    }

    public Map<UsageType, Double> aggregateByType() {
        List<UsageRecord> records = usageRepository.findAll();
        return records.stream()
                .filter(r -> r.getType() != null)
                .collect(Collectors.groupingBy(
                        UsageRecord::getType,
                        Collectors.summingDouble(r -> {
                            if (r.getType() == UsageType.PRODUCER && r.getProducedKw() != null) {
                                return r.getProducedKw();
                            } else if (r.getType() == UsageType.USER && r.getUsedKw() != null) {
                                return r.getUsedKw();
                            }
                            return 0.0;
                        })
                ));
    }

    public void aggregateUsage(UsageRecord record) {
        ZonedDateTime hour = record.getTimestamp().atZone(ZoneId.systemDefault()).withMinute(0).withSecond(0).withNano(0);
        Instant hourInstant = hour.toInstant();

        List<UsageRecord> hourRecords = usageRepository.findAll().stream()
                .filter(r -> r.getTimestamp() != null &&
                        r.getTimestamp().atZone(ZoneId.systemDefault()).withMinute(0).withSecond(0).withNano(0).equals(hour))
                .collect(Collectors.toList());

        double communityProduced = hourRecords.stream()
                .filter(r -> r.getType() == UsageType.PRODUCER && r.getProducedKw() != null)
                .mapToDouble(UsageRecord::getProducedKw)
                .sum();

        double communityUsed = hourRecords.stream()
                .filter(r -> r.getType() == UsageType.USER && r.getUsedKw() != null)
                .mapToDouble(r -> r.getUsedKw())
                .sum();

        double gridUsed = Math.max(0, communityUsed - communityProduced);

        // Add logging here
        System.out.printf("Aggregating for hour=%s, produced=%.2f, used=%.2f, grid=%.2f%n",
                hour, communityProduced, communityUsed, gridUsed);

        UpdateMessage update = new UpdateMessage();
        update.setHour(hourInstant.toEpochMilli());
        update.setCommunityProduced(communityProduced);
        update.setCommunityUsed(communityUsed);
        update.setGridUsed(gridUsed);

        amqpTemplate.convertAndSend("energy-exchange", "usage.update", update);
    }
}