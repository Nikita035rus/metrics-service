package com.mtp.metricsservice.service;

import com.mtp.metricsservice.dto.MetricRequest;
import com.mtp.metricsservice.dto.MetricsStatsResponse;

import java.time.OffsetDateTime;

public interface MetricsService {
    void saveMetric(MetricRequest request, String clientId);
    MetricsStatsResponse getMetricsStats(OffsetDateTime from, OffsetDateTime to, String clientId);
}

