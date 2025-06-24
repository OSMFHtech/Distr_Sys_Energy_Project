package com.Distr_Sys.usage.service;

import com.Distr_Sys.usage.model.UsageRecord;
import com.Distr_Sys.usage.model.UsageType;
import com.Distr_Sys.usage.repository.UsageRepository;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsageServiceTest {
    @Test
    void testAggregateUsageByType() {
        UsageRepository repo = mock(UsageRepository.class);
        RabbitTemplate rabbit = mock(RabbitTemplate.class);
        AggregationService aggregationService = mock(AggregationService.class);

        List<Object[]> mockResult = Arrays.asList(
                new Object[]{UsageType.USER, 100.0},
                new Object[]{UsageType.PRODUCER, 200.0}
        );
        when(repo.aggregateUsageByType()).thenReturn(mockResult);

        UsageService service = new UsageService(repo, rabbit, aggregationService);
        Map<UsageType, Double> result = service.aggregateUsageByType();

        assertEquals(100.0, result.get(UsageType.USER));
        assertEquals(200.0, result.get(UsageType.PRODUCER));
    }
}