package com.Distr_Sys.percentage.client;

import com.Distr_Sys.percentage.model.ProducerDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "producer-service", url = "${producer.url}")
public interface ProducerClient {
    @GetMapping("/producer/current")
    ProducerDTO current();
}
