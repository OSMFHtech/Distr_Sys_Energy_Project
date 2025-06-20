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
    public ResponseEntity<Map<String, Object>> consume(@RequestBody Map<String, Object> req) {
        Long userId = Long.valueOf(req.get("userId").toString());
        Double m = Double.valueOf(req.get("m").toString());
        ConsumptionRecord rec = service.consume(userId, m);
        return ResponseEntity.ok(Map.of(
                "message", "Consumption recorded",
                "id", rec.getId(),
                "timestamp", rec.getDatetime(),
                "consumedKw", rec.getKwh()
        ));
    }

    @GetMapping("/consume/latest")
    public ResponseEntity<ConsumptionRecord> latest() {
        return ResponseEntity.ok(service.latestConsumption());
    }

    @GetMapping("/consume/all")
    public List<ConsumptionRecord> all() {
        return service.getAllConsumption();
    }
}