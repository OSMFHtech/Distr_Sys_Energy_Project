package com.Energy.API.service;

import com.Energy.API.model.PercentageEntry;
import com.Energy.API.model.UsageEntry;
import com.Energy.API.repository.PercentageRepository;
import com.Energy.API.repository.UsageRepository;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.HashMap;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EnergyService {
    private final UsageRepository usageRepository;
    private final PercentageRepository percentageRepository;

    public EnergyService(UsageRepository usageRepository, PercentageRepository percentageRepository) {
        this.usageRepository = usageRepository;
        this.percentageRepository = percentageRepository;
    }

    public PercentageEntry getCurrentPercentage() {
        return percentageRepository.findAll().stream()
                .max((a, b) -> a.getHour().compareTo(b.getHour()))
                .orElse(null);
    }

    public List<UsageEntry> getHistoricalUsage(LocalDateTime start, LocalDateTime end) {
        return usageRepository.findByHourBetween(start, end);
    }
    public Map<String, Double> getAggregatedData(LocalDateTime start, LocalDateTime end) {
        List<UsageEntry> usages = usageRepository.findByHourBetween(start, end);
        double produced = usages.stream().mapToDouble(UsageEntry::getCommunityProduced).sum();
        double used = usages.stream().mapToDouble(UsageEntry::getCommunityUsed).sum();
        double gridUsed = usages.stream().mapToDouble(UsageEntry::getGridUsed).sum();
        Map<String, Double> result = new HashMap<>();
        result.put("produced", produced);
        result.put("used", used);
        result.put("gridUsed", gridUsed);
        return result;
    }
}