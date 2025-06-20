package com.Distr_Sys.usage.web;

import com.Distr_Sys.usage.model.UsageRecord;
import com.Distr_Sys.usage.service.UsageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usage")
public class UsageController {
    private final UsageService svc;
    public UsageController(UsageService svc) { this.svc = svc; }

    @PostMapping("/publish")
    public ResponseEntity<UsageRecord> publish(
            @RequestParam Long userId,
            @RequestParam int usedKw
    ) {
        return ResponseEntity.ok(svc.recordUsage(userId, usedKw));
    }

    @GetMapping("/{userId}")
    public UsageRecord latest(@PathVariable Long userId) {
        return svc.latestForUser(userId);
    }
}