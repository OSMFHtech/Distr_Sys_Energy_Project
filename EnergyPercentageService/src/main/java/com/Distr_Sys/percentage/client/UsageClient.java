package com.Distr_Sys.percentage.client;

import com.Distr_Sys.percentage.model.UsageDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "usage-service", url = "${usage.url}")
public interface UsageClient {
    @GetMapping("/usage/{userId}")
    UsageDTO latest(@PathVariable("userId") Long userId);
}
