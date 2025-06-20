package com.Distr_Sys.usage.service;

import com.Distr_Sys.usage.config.RabbitConfig;
import com.Distr_Sys.usage.model.UsageRecord;
import com.Distr_Sys.usage.repository.UsageRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import java.time.Instant;

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
        rec = repo.save(rec);
        rabbit.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.ROUTING_KEY, rec);
        return rec;
    }

    public UsageRecord latestForUser(Long userId) {
        return repo.findTopByUserIdOrderByTimestampDesc(userId)
                   .orElse(new UsageRecord(userId, Instant.now(), 0));
    }
}
