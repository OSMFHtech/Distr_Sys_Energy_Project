package com.Distr_Sys.percentage.service;

import com.Distr_Sys.percentage.model.UpdateMessage;
import com.Distr_Sys.percentage.model.PercentageRecord;
import com.Distr_Sys.percentage.repository.PercentageRecordRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class UpdateListener {
    private final PercentageRecordRepository percentageRecordRepository;

    public UpdateListener(PercentageRecordRepository percentageRecordRepository) {
        this.percentageRecordRepository = percentageRecordRepository;
    }

    @RabbitListener(queues = "${percentage.rabbitmq.usage-update-queue:usage.update}")
    @Transactional
    public void handleUpdate(UpdateMessage message) {
        if (message.getHour() == null) {
            return;
        }
        LocalDateTime hour = LocalDateTime.parse(message.getHour(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        double produced = message.getCommunityProduced();
        double used = message.getCommunityUsed();
        double grid = message.getGridUsed(); // use for calculations

        double communityDepleted = (produced > 0) ? Math.min(100.0, (used / produced) * 100.0) : 0.0;
        double totalUsed = used + grid;
        double gridPortion = (totalUsed > 0) ? Math.min(100.0, (grid / totalUsed) * 100.0) : 0.0;

        // Round to 2 decimals for display
        communityDepleted = Math.round(communityDepleted * 100.0) / 100.0;
        gridPortion = Math.round(gridPortion * 100.0) / 100.0;

        // Ensure gridPortion is never 0% if grid was used
        if (grid > 0 && gridPortion == 0.0) {
            gridPortion = 0.01;
        }

        PercentageRecord record = percentageRecordRepository.findByHour(hour)
                .orElse(new PercentageRecord(hour, 0, 0));
        record.setHour(hour);
        record.setCommunityDepleted(communityDepleted);
        record.setGridPortion(gridPortion);

        percentageRecordRepository.save(record);
    }
}