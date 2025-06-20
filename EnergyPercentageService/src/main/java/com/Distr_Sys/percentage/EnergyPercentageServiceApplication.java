package com.Distr_Sys.percentage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class EnergyPercentageServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(EnergyPercentageServiceApplication.class, args);
    }
}
