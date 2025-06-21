package com.Distr_Sys.usage.web;

import com.Distr_Sys.usage.model.UsageRecord;
import com.Distr_Sys.usage.model.UsageType;
import com.Distr_Sys.usage.service.AggregationService;
import com.Distr_Sys.usage.service.UsageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/usage")
public class UsageController {
    private final UsageService usageService;
    private final AggregationService aggregationService;

    public UsageController(UsageService usageService, AggregationService aggregationService) {
        this.usageService = usageService;
        this.aggregationService = aggregationService;
    }

    @PostMapping("/publish")
    public ResponseEntity<UsageRecord> publish(
            @RequestParam Long userId,
            @RequestParam int usedKw
    ) {
        return ResponseEntity.ok(usageService.recordUsage(userId, usedKw));
    }

    @GetMapping("/{userId}")
    public UsageRecord latest(@PathVariable Long userId) {
        return usageService.latestForUser(userId);
    }

    @GetMapping("/aggregate/by-type")
    public Map<UsageType, Double> aggregateByType() {
        return aggregationService.aggregateByType();
    }
}