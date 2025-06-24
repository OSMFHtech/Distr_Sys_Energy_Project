
package com.Distr_Sys.usage.service;

import com.Distr_Sys.usage.model.HourlyUsage;
import com.Distr_Sys.usage.repository.HourlyUsageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HourlyUsageService {
    private final HourlyUsageRepository hourlyUsageRepository;

    public HourlyUsageService(HourlyUsageRepository hourlyUsageRepository) {
        this.hourlyUsageRepository = hourlyUsageRepository;
    }

    public List<HourlyUsage> getAllHourlyUsage() {
        return hourlyUsageRepository.findAll();
    }
}