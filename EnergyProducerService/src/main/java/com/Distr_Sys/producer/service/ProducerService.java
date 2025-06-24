package com.Distr_Sys.producer.service;

import com.Distr_Sys.producer.config.RabbitConfig;
import com.Distr_Sys.producer.model.ProductionRecord;
import com.Distr_Sys.producer.repository.ProductionRepository;
import com.Distr_Sys.common.EnergyMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProducerService {
    private final ProductionRepository repo;
    private final RabbitTemplate rabbit;
    private static final Long PRODUCER_ID = 1L;

    public ProducerService(ProductionRepository repo, RabbitTemplate rabbit) {
        this.repo = repo;
        this.rabbit = rabbit;
    }

    public ProductionRecord produce(double kw) {
        LocalDateTime now = LocalDateTime.now();
        ProductionRecord rec = new ProductionRecord(
                PRODUCER_ID,
                kw,
                now
        );
        rec = repo.save(rec);

        EnergyMessage msg = new EnergyMessage(
                EnergyMessage.Type.PRODUCER,
                PRODUCER_ID,
                now,
                rec.getKwh()
        );
        rabbit.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.ROUTING_KEY, msg);
        System.out.printf("[Producer] Sent message: type=%s, userId=%d, kwh=%.3f, datetime=%s%n",
                msg.getType(), msg.getUserId(), msg.getKwh(), msg.getDatetime());
        return rec;
    }

    public ProductionRecord latest() {
        return repo.findTopByOrderByDatetimeDesc()
                .orElse(new ProductionRecord(
                        PRODUCER_ID,
                        0.0,
                        LocalDateTime.now()
                ));
    }

    public List<ProductionRecord> findAll() {
        return repo.findAll();
    }
}