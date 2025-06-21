package com.Distr_Sys.usage.service;

import com.Distr_Sys.usage.config.RabbitConfig;
import com.Distr_Sys.usage.model.UsageRecord;
import com.Distr_Sys.usage.model.UsageType;
import com.Distr_Sys.usage.repository.UsageRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import java.time.Instant;
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

    public UsageRecord recordUsage(Long userId, int usedKw) {
        UsageRecord rec = new UsageRecord(userId, Instant.now(), usedKw);
        rec.setProducedKw(null);
        rec.setType(UsageType.USER); // Ensure type is set
        rec = repo.save(rec);
        rabbit.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.ROUTING_KEY, rec);
        return rec;
    }

    public UsageRecord latestForUser(Long userId) {
        return repo.findTopByUserIdOrderByTimestampDesc(userId)
                .orElse(new UsageRecord(userId, Instant.now(), 0));
    }

    public Map<String, Integer> aggregateUsageByType() {
        List<Object[]> results = repo.aggregateUsageByType();
        Map<String, Integer> summary = new HashMap<>();
        for (Object[] row : results) {
            summary.put(row[0].toString(), ((Number) row[1]).intValue());
        }
        return summary;
    }
}