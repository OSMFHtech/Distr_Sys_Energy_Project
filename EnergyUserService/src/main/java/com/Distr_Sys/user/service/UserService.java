package com.Distr_Sys.user.service;

import com.Distr_Sys.user.config.RabbitConfig;
import com.Distr_Sys.user.model.ConsumptionRecord;
import com.Distr_Sys.user.model.UserProfile;
import com.Distr_Sys.user.repository.ConsumptionRepository;
import com.Distr_Sys.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
public class UserService {
    private final UserRepository userRepo;
    private final ConsumptionRepository consRepo;
    private final RabbitTemplate rabbit;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Random random = new Random();

    public UserService(UserRepository userRepo, ConsumptionRepository consRepo, RabbitTemplate rabbit) {
        this.userRepo = userRepo;
        this.consRepo = consRepo;
        this.rabbit = rabbit;
    }

    public UserProfile createProfile(UserProfile profile) {
        UserProfile saved = userRepo.save(profile);
        return saved;
    }

    public Optional<UserProfile> findProfile(Long id) {
        return userRepo.findById(id);
    }

    public List<UserProfile> getAllProfiles() {
        return userRepo.findAll();
    }

    public ConsumptionRecord consume(Long userId, Double m) {
        LocalDateTime now = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());
        int hour = now.getHour();
        double kwh;
        if ((hour >= 6 && hour < 10) || (hour >= 18 && hour < 22)) {
            kwh = m * random.nextDouble();
        } else {
            kwh = m * random.nextDouble() * 0.2;
        }
        ConsumptionRecord rec = new ConsumptionRecord(userId, kwh, now);
        rec = consRepo.save(rec);
        try {
            Map<String, Object> message = new HashMap<>();
            message.put("type", "USER");
            message.put("userId", userId);
            message.put("kwh", rec.getKwh());
            message.put("datetime", rec.getDatetime().toString());
            String json = objectMapper.writeValueAsString(message);
            rabbit.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.ROUTING_KEY, json);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize ConsumptionRecord", e);
        }
        return rec;
    }

    public ConsumptionRecord latestConsumption() {
        return consRepo.findTopByOrderByDatetimeDesc()
                .orElse(new ConsumptionRecord(1L, 0.0, LocalDateTime.now()));
    }

    public List<ConsumptionRecord> getAllConsumption() {
        return consRepo.findAll();
    }
}