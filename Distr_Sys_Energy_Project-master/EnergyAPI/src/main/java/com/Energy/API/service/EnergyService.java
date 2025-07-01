package com.Energy.API.service;

import com.Energy.API.model.PercentageEntry;
import com.Energy.API.model.UsageEntry;
import com.Energy.API.repository.PercentageRepository;
import com.Energy.API.repository.UsageRepository;
import org.springframework.stereotype.Service;

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

    public List<UsageEntry> getAllUsage() {
        return usageRepository.findAll();
    }
}