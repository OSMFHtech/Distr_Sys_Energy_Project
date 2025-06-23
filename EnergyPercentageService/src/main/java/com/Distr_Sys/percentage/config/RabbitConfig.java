package com.Distr_Sys.percentage.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;

@Configuration
public class RabbitConfig {

    @Value("${percentage.rabbitmq.usage-update-queue:usage.update}")
    private String usageUpdateQueueName;

    @Bean
    public Queue usageUpdateQueue() {
        return new Queue(usageUpdateQueueName, true, false, false);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange("energy-exchange");
    }

    @Bean
    public Binding usageUpdateBinding(Queue usageUpdateQueue, TopicExchange exchange) {
        return BindingBuilder.bind(usageUpdateQueue).to(exchange).with("usage.update");
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}