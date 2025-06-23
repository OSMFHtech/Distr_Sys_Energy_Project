package com.Distr_Sys.percentage.service;

import com.Distr_Sys.percentage.model.PercentageRecord;
import com.Distr_Sys.percentage.model.UpdateMessage;
import com.Distr_Sys.percentage.repository.PercentageRecordRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class PercentageService {
    private final PercentageRecordRepository repository;

    public PercentageService(PercentageRecordRepository repository) {
        this.repository = repository;
    }

    @RabbitListener(queues = "${percentage.rabbitmq.usage-update-queue}")
    @Transactional
    public void handleUpdate(UpdateMessage msg) {
        // Use Instant directly
        Instant hourInstant = msg.getHour();
        double totalUsed = msg.getCommunityUsed() + msg.getGridUsed();
        double communityDepleted = (msg.getCommunityProduced() <= msg.getCommunityUsed()) ? 100.0 : (msg.getCommunityUsed() / msg.getCommunityProduced()) * 100.0;
        double gridPortion = (totalUsed == 0) ? 0.0 : (msg.getGridUsed() / totalUsed) * 100.0;

        PercentageRecord record = repository.findByHour(hourInstant)
                .orElse(new PercentageRecord());
        record.setHour(hourInstant);
        record.setCommunityDepleted(communityDepleted);
        record.setGridPortion(gridPortion);

        repository.save(record);
    }

    public double getPercentage(Long userId) {
        return repository.findAll().stream()
                .sorted((a, b) -> b.getHour().compareTo(a.getHour()))
                .findFirst()
                .map(PercentageRecord::getGridPortion)
                .orElse(0.0);
    }
}