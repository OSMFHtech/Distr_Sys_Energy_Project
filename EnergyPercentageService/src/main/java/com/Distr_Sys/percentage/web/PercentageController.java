package com.Distr_Sys.percentage.web;

import com.Distr_Sys.percentage.model.PercentageRecord;
import com.Distr_Sys.percentage.service.PercentageService;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/percentage")
public class PercentageController {
    private final PercentageService percentageService;

    public PercentageController(PercentageService percentageService) {
        this.percentageService = percentageService;
    }

    @GetMapping("/current")
    public Map<String, String> getCurrentPercentage() {
        PercentageRecord record = percentageService.getCurrentPercentage();
        if (record == null) {
            return Map.of("message", "No percentage record found");
        }
        Map<String, String> result = new HashMap<>();
        result.put("hour", record.getHour().toString());
        result.put("community_depleted", String.format("%.2f", record.getCommunityDepleted()));
        result.put("grid_portion", String.format("%.2f", record.getGridPortion()));
        return result;
    }

    @GetMapping("/all")
    public List<PercentageRecord> getAllPercentages() {
        return percentageService.getAllPercentages();
    }
}