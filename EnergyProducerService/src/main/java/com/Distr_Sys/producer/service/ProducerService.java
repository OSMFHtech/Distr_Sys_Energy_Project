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

    public ProducerService(ProductionRepository repo, RabbitTemplate rabbit) {
        this.repo = repo;
        this.rabbit = rabbit;
    }

    public ProductionRecord produce(double kw) {
        ProductionRecord rec = new ProductionRecord(
                1, // producerId
                kw,
                LocalDateTime.now()
        );
        rec = repo.save(rec);

        EnergyMessage msg = new EnergyMessage(
                EnergyMessage.Type.PRODUCER,
                1L,
                LocalDateTime.now(),
                rec.getKwh()
        );
        rabbit.convertAndSend(RabbitConfig.EXCHANGE, RabbitConfig.ROUTING_KEY, msg);
        System.out.println("[Producer] Sent message: type=" + msg.getType() +
                ", userId=" + msg.getUserId() +
                ", kwh=" + msg.getKwh() +
                ", datetime=" + msg.getDatetime());
        return rec;
    }

    public ProductionRecord latest() {
        return repo.findTopByOrderByDatetimeDesc()
                .orElse(new ProductionRecord(
                        1,
                        0.0,
                        LocalDateTime.now()
                ));
    }

    public List<ProductionRecord> findAll() {
        return repo.findAll();
    }
}