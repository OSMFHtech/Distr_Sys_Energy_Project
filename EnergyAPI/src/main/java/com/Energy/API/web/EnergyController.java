package com.Energy.API.web;

import com.Energy.API.model.PercentageEntry;
import com.Energy.API.model.UsageEntry;
import com.Energy.API.service.EnergyService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/energy")
@CrossOrigin
public class EnergyController {

    private final EnergyService energyService;

    public EnergyController(EnergyService energyService) {
        this.energyService = energyService;
    }

    @GetMapping("/current")
    public PercentageEntry getCurrent() {
        return energyService.getCurrentPercentage();
    }

    @GetMapping("/historical")
    public List<UsageEntry> getHistorical(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return energyService.getHistoricalUsage(start, end);
    }

    @GetMapping("/data")
    public Map<String, Double> getEnergyData(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return energyService.getAggregatedData(start, end);
    }
}