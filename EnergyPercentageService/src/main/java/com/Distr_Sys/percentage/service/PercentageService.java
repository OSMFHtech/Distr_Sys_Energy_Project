package com.Distr_Sys.percentage.service;

import com.Distr_Sys.percentage.model.PercentageRecord;
import com.Distr_Sys.percentage.shared.UpdateMessage;
import com.Distr_Sys.percentage.repository.PercentageRecordRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class PercentageService {
    private final PercentageRecordRepository repository;

    public PercentageService(PercentageRecordRepository repository) {
        this.repository = repository;
    }

    @RabbitListener(queues = "${percentage.rabbitmq.usage-update-queue}")
    @Transactional
    public void handleUpdate(UpdateMessage msg) {
        if (msg == null) {
            System.err.println("Received null UpdateMessage!");
            return;
        }
        System.out.printf(
                "Received UpdateMessage: hour=%d, produced=%.2f, used=%.2f, grid=%.2f%n",
                msg.getHour(), msg.getCommunityProduced(), msg.getCommunityUsed(), msg.getGridUsed()
        );

        LocalDateTime hour = LocalDateTime.ofInstant(
                java.time.Instant.ofEpochMilli(msg.getHour()), ZoneId.systemDefault()
        );
        double totalUsed = msg.getCommunityUsed() + msg.getGridUsed();

        // Fix: Cap at 100%, show 0% if nothing produced
        double communityDepleted = (msg.getCommunityProduced() == 0)
                ? 0.0
                : Math.min(100.0, (msg.getCommunityUsed() / msg.getCommunityProduced()) * 100.0);

        double gridPortion = (totalUsed == 0) ? 0.0 : (msg.getGridUsed() / totalUsed) * 100.0;

        PercentageRecord record = repository.findByHour(hour)
                .orElse(new PercentageRecord());
        record.setHour(hour);
        record.setCommunityDepleted(communityDepleted);
        record.setGridPortion(gridPortion);

        repository.save(record);
        System.out.println("Saved PercentageRecord for hour: " + hour);
    }

    public List<PercentageRecord> getAllPercentages() {
        return repository.findAll();
    }
    public PercentageRecord getCurrentPercentage() {
        return repository.findAll().stream()
                .max(java.util.Comparator.comparing(PercentageRecord::getHour))
                .orElse(null);
    }
}