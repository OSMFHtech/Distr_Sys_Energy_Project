package com.Distr_Sys.usage.service;

import com.Distr_Sys.usage.model.UsageRecord;
import com.Distr_Sys.usage.model.UsageType;
import com.Distr_Sys.usage.repository.UsageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AggregationService {
    private final UsageRepository usageRepository;

    public AggregationService(UsageRepository usageRepository) {
        this.usageRepository = usageRepository;
    }

    public Map<UsageType, Double> aggregateByType() {
        List<UsageRecord> records = usageRepository.findAll();
        return records.stream()
                .filter(r -> r.getType() != null) // Filter out null types
                .collect(Collectors.groupingBy(
                        UsageRecord::getType,
                        Collectors.summingDouble(r -> {
                            if (r.getType() == UsageType.PRODUCER && r.getProducedKw() != null) {
                                return r.getProducedKw();
                            } else if (r.getType() == UsageType.USER && r.getUsedKw() != null) {
                                return r.getUsedKw();
                            }
                            return 0.0;
                        })
                ));
    }

    public void aggregateUsage(UsageRecord record) {
        // Implement if you need real-time or periodic aggregation
    }
}