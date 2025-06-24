package com.Distr_Sys.user.web;

import com.Distr_Sys.user.model.ConsumptionRecord;
import com.Distr_Sys.user.model.UserProfile;
import com.Distr_Sys.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/profile")
    public ResponseEntity<UserProfile> createProfile(@RequestBody UserProfile profile) {
        return ResponseEntity.ok(service.createProfile(profile));
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<UserProfile> getProfile(@PathVariable Long id) {
        return service.findProfile(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/profile")
    public List<UserProfile> getAllProfiles() {
        return service.getAllProfiles();
    }

    @PostMapping("/consume")
    public ResponseEntity<Map<String, Object>> consume(@RequestParam Long userId, @RequestParam Double maxKwh) {
        ConsumptionRecord rec = service.consume(userId, maxKwh);
        return ResponseEntity.ok(Map.of(
                "message", "Consumption message published",
                "id", rec.getId(),
                "timestamp", rec.getDatetime(),
                "usedKw", String.format("%.3f", rec.getKwh())
        ));
    }

    @GetMapping("/consumption/latest")
    public ConsumptionRecord latest() {
        return service.latestConsumption();
    }

    @GetMapping("/consumption/all")
    public List<ConsumptionRecord> all() {
        return service.getAllConsumption();
    }
}