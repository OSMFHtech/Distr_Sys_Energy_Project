// EnergyPercentageService/src/main/java/com/Distr_Sys/percentage/config/RabbitConfig.java
package com.Distr_Sys.percentage.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Value("${percentage.rabbitmq.usage-update-queue:usage.update}")
    private String usageUpdateQueueName;

    @Bean
    public Queue usageUpdateQueue() {
        // durable, not exclusive, not auto-delete
        return new Queue(usageUpdateQueueName, true, false, false);
    }
}