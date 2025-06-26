package com.Distr_Sys.percentage.service;

import com.Distr_Sys.percentage.model.UpdateMessage;
import com.Distr_Sys.percentage.model.PercentageRecord;
import com.Distr_Sys.percentage.repository.PercentageRecordRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class UpdateListener {
    private final PercentageRecordRepository percentageRecordRepository;

    public UpdateListener(PercentageRecordRepository percentageRecordRepository) {
        this.percentageRecordRepository = percentageRecordRepository;
    }

    @RabbitListener(queues = "${percentage.rabbitmq.usage-update-queue:usage.update}")
    @Transactional
    public void handleUpdate(UpdateMessage message) {
        // Parse hour
        LocalDateTime hour = LocalDateTime.parse(message.getHour());

        double produced = message.getCommunityProduced();
        double used = message.getCommunityUsed();
        double grid = message.getGridUsed();

        // Community depleted: 100% if all produced energy is used, else (used/produced)*100
        double communityDepleted = produced == 0 ? 0 : Math.min(100.0, (used / produced) * 100.0);

        // Grid portion: (grid used) / (community used + grid used) * 100
        double totalUsed = used + grid;
        double gridPortion = totalUsed == 0 ? 0 : (grid / totalUsed) * 100.0;

        // Only keep one record per hour (overwrite if exists)
        PercentageRecord record = percentageRecordRepository.findByHour(hour)
                .orElse(new PercentageRecord(hour, 0, 0));
        record.setCommunityDepleted(communityDepleted);
        record.setGridPortion(gridPortion);

        percentageRecordRepository.save(record);
    }
}