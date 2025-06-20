package com.Distr_Sys.producer.web;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class ProducerClient {
    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8181/producer/publish";

        ProductionRequest request = new ProductionRequest();
        request.setProducedKw(5);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ProductionRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        System.out.println(response.getBody());
    }
}