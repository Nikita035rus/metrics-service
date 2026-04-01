package com.mtp.metricsservice.integration;

import com.mtp.metricsservice.dto.MetricRequest;
import com.mtp.metricsservice.dto.MetricsStatsResponse;
import com.mtp.metricsservice.service.MetricsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
class MetricsServiceIntegrationTest {

    @Autowired
    private MetricsService metricsService;

    @Test
    void shouldCalculateCorrectStats() {
        String clientId = "test-client";
        OffsetDateTime now = OffsetDateTime.now();

        java.util.Map<String, Object> emptyPayload = java.util.Collections.emptyMap();

        metricsService.saveMetric(new MetricRequest(now, 10.0, emptyPayload), clientId);
        metricsService.saveMetric(new MetricRequest(now.plusMinutes(1), 20.0, emptyPayload), clientId);

        MetricsStatsResponse stats = metricsService.getMetricsStats(now.minusMinutes(1), now.plusMinutes(2), clientId);

        assertNotNull(stats);
        assertEquals(2L, stats.count());
        assertEquals(15.0, stats.avg());
        assertEquals(10.0, stats.min());
        assertEquals(20.0, stats.max());
    }

}

