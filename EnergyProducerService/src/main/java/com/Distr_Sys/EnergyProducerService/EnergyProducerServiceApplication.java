package com.Distr_Sys.EnergyProducerService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestTemplate;
import com.Distr_Sys.producer.model.ProductionRecord;
import com.Distr_Sys.producer.repository.ProductionRepository;
import java.time.LocalDateTime;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.Distr_Sys.producer.repository")
@EntityScan(basePackages = "com.Distr_Sys.producer.model")
@ComponentScan(basePackages = "com.Distr_Sys")
public class EnergyProducerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EnergyProducerServiceApplication.class, args);
	}

	@Bean
	CommandLineRunner initDb(ProductionRepository repo) {
		return args -> {
			repo.save(new ProductionRecord(1, 0.005, LocalDateTime.now()));
			repo.save(new ProductionRecord(2, 0.007, LocalDateTime.now().minusMinutes(1)));
		};
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}