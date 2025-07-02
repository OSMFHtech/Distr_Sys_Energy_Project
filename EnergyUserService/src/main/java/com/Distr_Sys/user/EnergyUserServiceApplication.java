package com.Distr_Sys.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EnergyUserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EnergyUserServiceApplication.class, args);
	}
}