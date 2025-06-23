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

    // New: Accept JSON body for user usage
    @PostMapping("/publish/json")
    public ResponseEntity<UsageRecord> publishJson(@RequestBody UsageRecord usage) {
        return ResponseEntity.ok(usageService.recordUsage(usage.getUserId(), usage.getUsedKw()));
    }

    @GetMapping("/{userId}")
    public UsageRecord latest(@PathVariable Long userId) {
        return usageService.latestForUser(userId);
    }

    @GetMapping("/aggregate/by-type")
    public Map<UsageType, Double> aggregateByType() {
        return aggregationService.aggregateByType();
    }

    @PostMapping("/aggregate/manual")
    public ResponseEntity<String> manualAggregate() {
        UsageRecord latest = usageService.findLatestRecord();
        if (latest == null) {
            return ResponseEntity.badRequest().body("No usage records found");
        }
        aggregationService.aggregateUsage(latest);
        return ResponseEntity.ok("Aggregation triggered");
    }
}