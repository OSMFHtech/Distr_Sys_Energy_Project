package com.Distr_Sys.percentage.web;

import com.Distr_Sys.percentage.service.PercentageService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/percentage")
public class PercentageController {
    private final PercentageService percentageService;

    public PercentageController(PercentageService percentageService) {
        this.percentageService = percentageService;
    }

    @GetMapping("/{userId}")
    public double getPercentage(@PathVariable Long userId) {
        return percentageService.getPercentage(userId);
    }
}