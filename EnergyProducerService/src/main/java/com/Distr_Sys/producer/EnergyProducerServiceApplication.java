package com.Distr_Sys.producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EnergyProducerServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(EnergyProducerServiceApplication.class, args);
    }
}