package com.Distr_Sys.user;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class UserQueueListener {
    @RabbitListener(queues = com.Distr_Sys.user.config.RabbitConfig.QUEUE)
    public void listen(String message) {
        System.out.println("Received from user-queue: " + message);
    }
}