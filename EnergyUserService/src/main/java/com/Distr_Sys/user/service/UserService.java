package com.Distr_Sys.user.service;

import com.Distr_Sys.user.config.RabbitConfig;
import com.Distr_Sys.user.model.ConsumptionRecord;
import com.Distr_Sys.user.model.UserProfile;
import com.Distr_Sys.user.repository.ConsumptionRepository;
import com.Distr_Sys.user.repository.UserRepository;
import com.Distr_Sys.common.EnergyMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class UserService {
    private final UserRepository userRepo;
    private final ConsumptionRepository consRepo;
    private final RabbitTemplate rabbit;
    private final Random random = new Random();

    public UserService(UserRepository userRepo, ConsumptionRepository consRepo, RabbitTemplate rabbit) {
        this.userRepo = userRepo;
        this.consRepo = consRepo;
        this.rabbit = rabbit;
    }

    public UserProfile createProfile(UserProfile profile) {
        return userRepo.save(profile);
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

        EnergyMessage msg = new EnergyMessage(
                EnergyMessage.Type.USER,
                userId,
                Instant.now(),
                rec.getKwh()
        );
        rabbit.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.ROUTING_KEY, msg);

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