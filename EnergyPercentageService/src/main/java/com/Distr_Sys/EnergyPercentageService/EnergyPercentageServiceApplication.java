package com.Distr_Sys.EnergyPercentageService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.Distr_Sys")
@EnableJpaRepositories("com.Distr_Sys.percentage.repository")
@EntityScan("com.Distr_Sys.percentage.model")
public class EnergyPercentageServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(EnergyPercentageServiceApplication.class, args);
	}
}