package com.Distr_Sys.usage.service;

import com.Distr_Sys.usage.model.HourlyUsage;
import com.Distr_Sys.usage.model.UsageRecord;
import com.Distr_Sys.usage.model.UsageType;
import com.Distr_Sys.usage.repository.HourlyUsageRepository;
import com.Distr_Sys.usage.repository.UsageRepository;
import com.Distr_Sys.usage.shared.UpdateMessage;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class AggregationService {
    private final UsageRepository usageRepository;
    private final HourlyUsageRepository hourlyUsageRepository;
    private final AmqpTemplate amqpTemplate;

    @Autowired
    public AggregationService(UsageRepository usageRepository, HourlyUsageRepository hourlyUsageRepository, AmqpTemplate amqpTemplate) {
        this.usageRepository = usageRepository;
        this.hourlyUsageRepository = hourlyUsageRepository;
        this.amqpTemplate = amqpTemplate;
    }

    public void aggregateUsage(UsageRecord record) {
        ZonedDateTime hour = record.getTimestamp().atZone(ZoneId.systemDefault()).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime hourLocal = hour.toLocalDateTime();

        List<UsageRecord> hourRecords = getRecordsForHour(hour);

        double communityProduced = hourRecords.stream()
                .filter(r -> r.getType() == UsageType.PRODUCER && r.getProducedKw() != null)
                .mapToDouble(UsageRecord::getProducedKw)
                .sum();

        double totalUserUsed = hourRecords.stream()
                .filter(r -> r.getType() == UsageType.USER && r.getUsedKw() != null)
                .mapToDouble(UsageRecord::getUsedKw)
                .sum();

        double communityUsed = Math.min(communityProduced, totalUserUsed);
        double gridUsed = Math.max(0, totalUserUsed - communityProduced);

        HourlyUsage hourlyUsage = hourlyUsageRepository.findByUsageHour(hourLocal)
                .orElse(new HourlyUsage(hourLocal));
        hourlyUsage.setCommunityProduced(communityProduced);
        hourlyUsage.setCommunityUsed(communityUsed);
        hourlyUsage.setGridUsed(gridUsed);
        hourlyUsageRepository.save(hourlyUsage);

        sendUpdateMessage(hour, communityProduced, communityUsed, gridUsed);
    }

    private List<UsageRecord> getRecordsForHour(ZonedDateTime hour) {
        return usageRepository.findAll().stream()
                .filter(r -> r.getTimestamp() != null &&
                        r.getTimestamp().atZone(ZoneId.systemDefault()).withMinute(0).withSecond(0).withNano(0).equals(hour))
                .toList();
    }

    private void sendUpdateMessage(ZonedDateTime hour, double communityProduced, double communityUsed, double gridUsed) {
        UpdateMessage update = new UpdateMessage();
        update.setHour(hour.toInstant().toEpochMilli());
        update.setCommunityProduced(communityProduced);
        update.setCommunityUsed(communityUsed);
        update.setGridUsed(gridUsed);
        amqpTemplate.convertAndSend("energy-exchange", "usage.update", update);
    }

    public Map<UsageType, Double> aggregateByType() {
        List<Object[]> results = usageRepository.aggregateUsageByType();
        Map<UsageType, Double> summary = new HashMap<>();
        for (Object[] row : results) {
            UsageType type = UsageType.valueOf(row[0].toString());
            Double value = row[1] != null ? ((Number) row[1]).doubleValue() : 0.0;
            summary.put(type, value);
        }
        return summary;
    }
}