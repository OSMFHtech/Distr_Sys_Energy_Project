package com.Distr_Sys.EnergyProducerService.web;

import com.Distr_Sys.producer.web.ProductionRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProducerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void publishShouldSendToRabbitMQ() throws Exception {
        ProductionRequest request = new ProductionRequest();
        request.setProducedKw(5);

        mockMvc.perform(post("/producer/publish")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}