
package com.Distr_Sys.usage.web;

import com.Distr_Sys.usage.model.UsageRecord;
import com.Distr_Sys.usage.model.UsageType;
import com.Distr_Sys.usage.service.AggregationService;
import com.Distr_Sys.usage.service.UsageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
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
    public ResponseEntity<Map<String, Object>> publish(
            @RequestParam Long userId,
            @RequestParam Double usedKw
    ) {
        UsageRecord rec = usageService.recordUsage(userId, usedKw);
        return ResponseEntity.ok(Map.of(
                "message", "Usage recorded",
                "id", rec.getId(),
                "timestamp", rec.getTimestamp(),
                "usedKw", String.format("%.3f", rec.getUsedKw())
        ));
    }

    @PostMapping("/publish/json")
    public ResponseEntity<Map<String, Object>> publishJson(@RequestBody UsageRecord usage) {
        UsageRecord rec = usageService.recordUsage(usage.getUserId(), usage.getUsedKw());
        return ResponseEntity.ok(Map.of(
                "message", "Usage recorded",
                "id", rec.getId(),
                "timestamp", rec.getTimestamp(),
                "usedKw", String.format("%.3f", rec.getUsedKw())
        ));
    }

    @GetMapping("/{userId}")
    public UsageRecord latest(@PathVariable Long userId) {
        return usageService.latestForUser(userId);
    }

    @GetMapping("/aggregate/by-type")
    public Map<String, String> aggregateByType() {
        Map<UsageType, Double> raw = aggregationService.aggregateByType();
        Map<String, String> formatted = new HashMap<>();
        raw.forEach((type, value) -> formatted.put(type.name(), String.format("%.3f", value)));
        return formatted;
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

    // --- FIX: Add endpoint to aggregate all hours ---
    @PostMapping("/aggregate/all")
    public ResponseEntity<String> aggregateAll() {
        aggregationService.aggregateAllHours();
        return ResponseEntity.ok("Aggregated all hours");
    }
}