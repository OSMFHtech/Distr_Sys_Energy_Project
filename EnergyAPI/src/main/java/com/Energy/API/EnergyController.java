package com.Energy.API;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/energy")
public class EnergyController {

    @Autowired
    private UsageRepository usageRepository;

    @Autowired
    private PercentageRepository percentageRepository;

    @GetMapping("/energy/data")
    public Map<String, Double> getEnergyData(@RequestParam("start") String start, @RequestParam("end") String end) {
        LocalDateTime startDate = LocalDateTime.parse(start + "T00:00:00");
        LocalDateTime endDate = LocalDateTime.parse(end + "T23:59:59");

        List<UsageEntry> usages = usageRepository.findAll().stream()
                .filter(e -> !e.getHour().isBefore(startDate) && !e.getHour().isAfter(endDate))
                .collect(Collectors.toList());

        double produced = usages.stream().mapToDouble(UsageEntry::getCommunityProduced).sum();
        double used = usages.stream().mapToDouble(UsageEntry::getCommunityUsed).sum();
        double gridUsed = usages.stream().mapToDouble(UsageEntry::getGridUsed).sum();

        Map<String, Double> result = new HashMap<>();
        result.put("produced", produced);
        result.put("used", used);
        result.put("gridUsed", gridUsed);
        return result;
    }
    @GetMapping("/current")
    public Map<String, Object> getCurrent() {
        List<Map<String, Object>> simulatedData = List.of(
                Map.of(
                        "hour", LocalDateTime.now().minusHours(2),
                        "produced", 100.0,
                        "used", 80.0,
                        "gridUsed", 20.0
                ),
                Map.of(
                        "hour", LocalDateTime.now().minusHours(1),
                        "produced", 120.0,
                        "used", 90.0,
                        "gridUsed", 30.0
                ),
                Map.of(
                        "hour", LocalDateTime.now(),
                        "produced", 150.0,
                        "used", 110.0,
                        "gridUsed", 40.0
                )
        );

        return simulatedData.get(simulatedData.size() - 1);
    }
}