/*package com.Distr_Sys.percentage.messaging;

import com.Distr_Sys.percentage.dto.HourlyUsage;
import com.Distr_Sys.percentage.model.PercentageRecord;
import com.Distr_Sys.percentage.repository.PercentageRecordRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PercentageListener {
    private final PercentageRecordRepository repo;

    public PercentageListener(PercentageRecordRepository repo) {
        this.repo = repo;
    }

    @RabbitListener(queues = "${percentage.rabbitmq.usage-update-queue}")
    public void handleHourlyUpdate(HourlyUsage usage) {
        double communityDepleted = usage.getCommunityProduced() < usage.getCommunityUsed() ? 100.0 : 0.0;
        double totalUsage = usage.getCommunityUsed() + usage.getGridUsed();
        double gridPortion = totalUsage > 0 ? (usage.getGridUsed() * 100.0 / totalUsage) : 0.0;

        // Convert LocalDateTime to Instant
        java.time.Instant usageHourInstant = usage.getUsageHour().atZone(java.time.ZoneId.systemDefault()).toInstant();

        PercentageRecord record = new PercentageRecord(
                usageHourInstant,
                communityDepleted,
                gridPortion
        );
        repo.save(record);
    }
}

 */