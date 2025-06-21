package com.Distr_Sys.percentage.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String EXCHANGE = "energy-exchange";
    public static final String HOURLY_UPDATE_ROUTING_KEY = "hourly.update";
    public static final String HOURLY_UPDATE_QUEUE = "hourly-update-queue";
    public static final String USAGE_UPDATE_QUEUE = "usage-update-queue";
    public static final String USAGE_UPDATE_ROUTING_KEY = "usage.update";

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Queue hourlyUpdateQueue() {
        return new Queue(HOURLY_UPDATE_QUEUE, true);
    }

    @Bean
    public Queue usageUpdateQueue() {
        return new Queue(USAGE_UPDATE_QUEUE, true);
    }

    @Bean
    public Binding hourlyUpdateBinding(Queue hourlyUpdateQueue, TopicExchange exchange) {
        return BindingBuilder.bind(hourlyUpdateQueue).to(exchange).with(HOURLY_UPDATE_ROUTING_KEY);
    }

    @Bean
    public Binding usageUpdateBinding(Queue usageUpdateQueue, TopicExchange exchange) {
        return BindingBuilder.bind(usageUpdateQueue).to(exchange).with(USAGE_UPDATE_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}