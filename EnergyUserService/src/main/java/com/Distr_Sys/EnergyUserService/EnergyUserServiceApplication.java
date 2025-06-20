// src/main/java/com/Distr_Sys/EnergyUserService/EnergyUserServiceApplication.java
package com.Distr_Sys.EnergyUserService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import com.Distr_Sys.user.model.UserProfile;
import com.Distr_Sys.user.repository.UserRepository;

@SpringBootApplication
@ComponentScan(basePackages = {"com.Distr_Sys.user", "com.Distr_Sys.EnergyUserService"})
@EnableJpaRepositories(basePackages = "com.Distr_Sys.user.repository")
@EntityScan(basePackages = "com.Distr_Sys.user.model")
public class EnergyUserServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(EnergyUserServiceApplication.class, args);
	}

	@Bean
	CommandLineRunner initDb(UserRepository userRepo) {
		return args -> {
			userRepo.save(new UserProfile("DefaultUser", "user@example.com"));
		};
	}
}