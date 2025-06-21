package com.Distr_Sys.percentage.web;

import com.Distr_Sys.percentage.service.PercentageService;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/percentage")
public class PercentageController {
    private final PercentageService service;

    public PercentageController(PercentageService service) {
        this.service = service;
    }

    @GetMapping("/{userId}")
    public Map<String, Object> getPercentage(@PathVariable Long userId) {
        double perc = service.getPercentage(userId);
        return Map.of("userId", userId, "percentage", perc);
    }
}