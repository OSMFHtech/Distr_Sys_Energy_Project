package com.Distr_Sys.percentage.service;

import com.Distr_Sys.percentage.client.ProducerClient;
import com.Distr_Sys.percentage.client.UsageClient;
import com.Distr_Sys.percentage.model.ProducerDTO;
import com.Distr_Sys.percentage.model.UsageDTO;
import org.springframework.stereotype.Service;

@Service
public class PercentageService {
    private final ProducerClient producerClient;
    private final UsageClient usageClient;

    public PercentageService(ProducerClient p, UsageClient u) {
        this.producerClient = p;
        this.usageClient = u;
    }

    public double getPercentage(Long userId) {
        ProducerDTO p = producerClient.current();
        UsageDTO u = usageClient.latest(userId);
        if (p.getProducedKw() > 0) {
            return u.getUsedKw() * 100.0 / p.getProducedKw();
        }
        return 0.0;
    }
}
