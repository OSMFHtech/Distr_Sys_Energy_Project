package com.Distr_Sys.percentage.service;

import com.Distr_Sys.percentage.model.PercentageRecord;
import com.Distr_Sys.percentage.model.UpdateMessage;
import com.Distr_Sys.percentage.repository.PercentageRecordRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PercentageService {
    private final PercentageRecordRepository repository;

    public PercentageService(PercentageRecordRepository repository) {
        this.repository = repository;
    }

    @RabbitListener(queues = "${percentage.rabbitmq.usage-update-queue}")
    @Transactional
    public void handleUpdate(UpdateMessage msg) {
        double totalUsed = msg.getCommunityUsed() + msg.getGridUsed();
        double communityDepleted = (msg.getCommunityProduced() <= msg.getCommunityUsed()) ? 100.0 : (msg.getCommunityUsed() / msg.getCommunityProduced()) * 100.0;
        double gridPortion = (totalUsed == 0) ? 0.0 : (msg.getGridUsed() / totalUsed) * 100.0;

        PercentageRecord record = repository.findByHour(msg.getHour())
                .orElse(new PercentageRecord());
        record.setHour(msg.getHour());
        record.setCommunityDepleted(communityDepleted);
        record.setGridPortion(gridPortion);

        repository.save(record);
    }

    // Add this method for the controller
    public double getPercentage(Long userId) {
        // Since PercentageRecord is not user-specific, return the latest gridPortion
        return repository.findAll().stream()
                .sorted((a, b) -> b.getHour().compareTo(a.getHour()))
                .findFirst()
                .map(PercentageRecord::getGridPortion)
                .orElse(0.0);
    }
}