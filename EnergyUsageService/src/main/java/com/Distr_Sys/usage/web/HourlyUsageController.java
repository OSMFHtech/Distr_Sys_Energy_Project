
package com.Distr_Sys.usage.web;

import com.Distr_Sys.usage.model.HourlyUsage;
import com.Distr_Sys.usage.service.HourlyUsageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/hourly-usage")
public class HourlyUsageController {
    private final HourlyUsageService hourlyUsageService;

    public HourlyUsageController(HourlyUsageService hourlyUsageService) {
        this.hourlyUsageService = hourlyUsageService;
    }

    @GetMapping("/all")
    public List<HourlyUsage> getAllHourlyUsage() {
        return hourlyUsageService.getAllHourlyUsage();
    }
}