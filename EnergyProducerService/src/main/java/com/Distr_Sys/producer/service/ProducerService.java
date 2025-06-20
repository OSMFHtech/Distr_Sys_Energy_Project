package com.Distr_Sys.producer.service;

import com.Distr_Sys.producer.config.RabbitConfig;
import com.Distr_Sys.producer.model.ProductionRecord;
import com.Distr_Sys.producer.repository.ProductionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProducerService {
    private final ProductionRepository repo;
    private final RabbitTemplate rabbit;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ProducerService(ProductionRepository repo, RabbitTemplate rabbit) {
        this.repo = repo;
        this.rabbit = rabbit;
    }

    public ProductionRecord produce(double kw) {
        ProductionRecord rec = new ProductionRecord(
                1, // producerId
                (double) kw,
                LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault())
        );
        rec = repo.save(rec);
        try {
            Map<String, Object> message = new HashMap<>();
            message.put("type", "PRODUCER");
            message.put("association", "COMMUNITY");
            message.put("kwh", rec.getKwh());
            message.put("datetime", rec.getDatetime().toString());
            String json = objectMapper.writeValueAsString(message);
            rabbit.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.ROUTING_KEY, json);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize ProductionRecord", e);
        }
        return rec;
    }

    public ProductionRecord latest() {
        return repo.findTopByOrderByDatetimeDesc()
                .orElse(new ProductionRecord(
                        1,
                        0.0,
                        LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault())
                ));
    }

    public List<ProductionRecord> findAll() {
        return repo.findAll();
    }
}