package com.Distr_Sys.usage.service;

import com.Distr_Sys.usage.model.*;
import com.Distr_Sys.usage.config.RabbitConfig;
import com.Distr_Sys.usage.repository.UsageRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UsageService {
    private final UsageRepository repo;
    private final RabbitTemplate rabbit;

    public UsageService(UsageRepository repo, RabbitTemplate rabbit) {
        this.repo = repo;
        this.rabbit = rabbit;
    }

    public UsageRecord recordUsage(Long userId, double usedKw) {
        UsageRecord rec = new UsageRecord(userId, LocalDateTime.now(), usedKw);
        rec.setProducedKw(null);
        rec.setType(UsageType.USER);
        rec = repo.save(rec);
        rabbit.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.ROUTING_KEY, rec);
        return rec;
    }

    public UsageRecord latestForUser(Long userId) {
        return repo.findTopByUserIdOrderByTimestampDesc(userId)
                .orElse(new UsageRecord(userId, LocalDateTime.now(), 0.0));
    }


    public UsageRecord findLatestRecord() {
        return repo.findTopByOrderByTimestampDesc().orElse(null);
    }

    public Map<String, Double> aggregateUsageByType() {
        List<Object[]> results = repo.aggregateUsageByType();
        Map<String, Double> summary = new HashMap<>();
        for (Object[] row : results) {
            summary.put(row[0].toString(), row[1] != null ? ((Number) row[1]).doubleValue() : 0.0);
        }
        return summary;
    }
}