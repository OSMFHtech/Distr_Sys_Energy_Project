package com.Distr_Sys.producer.config;

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
    public static final String ROUTING_KEY = "usage.new";
    public static final String QUEUE = "usage-queue";

    @Bean
    public TopicExchange exchange() {

        return new TopicExchange(EXCHANGE);
    }


    @Bean
    public Queue usageQueue() {

        return new Queue(QUEUE, true, false, false);
    }

    @Bean
    public Binding usageBinding(Queue usageQueue, TopicExchange exchange) {
        return BindingBuilder.bind(usageQueue).to(exchange).with(ROUTING_KEY);
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