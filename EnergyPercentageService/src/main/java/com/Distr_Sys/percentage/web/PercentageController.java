package com.Distr_Sys.percentage.web;

import java.util.List;
import com.Distr_Sys.percentage.model.PercentageRecord;
import com.Distr_Sys.percentage.service.PercentageService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/percentage")
public class PercentageController {
    private final PercentageService percentageService;

    public PercentageController(PercentageService percentageService) {
        this.percentageService = percentageService;
    }

    @GetMapping("/all")
    public List<PercentageRecord> getAllPercentages() {
        return percentageService.getAllPercentages();
    }
}