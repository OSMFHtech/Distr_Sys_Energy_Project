package com.Energy.API.web;

import com.Energy.API.model.PercentageEntry;
import com.Energy.API.model.UsageEntry;
import com.Energy.API.service.EnergyService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/energy")
@CrossOrigin
public class EnergyController {

    private final EnergyService energyService;

    public EnergyController(EnergyService energyService) {
        this.energyService = energyService;
    }

    @GetMapping("/current")
    public ResponseEntity<PercentageEntry> getCurrent() {
        PercentageEntry entry = energyService.getCurrentPercentage();
        if (entry == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(entry);
    }

    @GetMapping("/historical")
    public ResponseEntity<List<UsageEntry>> getHistorical(
            @RequestParam(value = "start", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(value = "end", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<UsageEntry> data;
        if (start != null && end != null) {
            data = energyService.getHistoricalUsage(start, end);
        } else {
            data = energyService.getAllUsage();
        }
        if (data == null || data.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(data);
    }
}