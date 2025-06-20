package com.Distr_Sys.EnergyUsageService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication(scanBasePackages = {"com.Distr_Sys.EnergyUsageService", "com.Distr_Sys.usage"})
@EnableJpaRepositories(basePackages = "com.Distr_Sys.usage.repository")
@EntityScan(basePackages = "com.Distr_Sys.usage.model")
public class EnergyUsageServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(EnergyUsageServiceApplication.class, args);
	}
}