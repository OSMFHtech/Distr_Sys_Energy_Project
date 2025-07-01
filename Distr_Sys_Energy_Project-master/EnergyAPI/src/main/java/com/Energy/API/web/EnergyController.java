package com.Energy.API.web;

import com.Energy.API.model.PercentageEntry;
import com.Energy.API.model.UsageEntry;
import com.Energy.API.repository.PercentageRepository;
import com.Energy.API.repository.UsageRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/energy")
@CrossOrigin
public class EnergyController {
    private final PercentageRepository percentageRepo;
    private final UsageRepository usageRepo;

    public EnergyController(PercentageRepository percentageRepo, UsageRepository usageRepo) {
        this.percentageRepo = percentageRepo;
        this.usageRepo = usageRepo;
    }

    @GetMapping("/current")
    public PercentageEntry getCurrent() {
        return percentageRepo.findAll().stream()
                .max((a, b) -> a.getHour().compareTo(b.getHour()))
                .orElse(null);
    }

    @GetMapping("/historical")
    public List<UsageEntry> getHistorical(
            @RequestParam(value = "start", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(value = "end", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        if (start != null && end != null) {
            return usageRepo.findByHourBetween(start, end);
        }
        return usageRepo.findAll();
    }
}