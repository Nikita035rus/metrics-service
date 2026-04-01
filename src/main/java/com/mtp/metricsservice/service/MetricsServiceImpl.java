package com.mtp.metricsservice.service;

import com.mtp.metricsservice.dto.MetricRequest;
import com.mtp.metricsservice.dto.MetricsStatsResponse;
import com.mtp.metricsservice.entity.Metric;
import com.mtp.metricsservice.repository.MetricRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MetricsServiceImpl implements MetricsService {

    private final MetricRepository metricRepository;

    @Transactional
    public void saveMetric(MetricRequest request, String clientId) {
        Metric metric = new Metric();
        metric.setTimestamp(request.timestamp());
        metric.setValue(request.value());
        metric.setPayload(request.payload());
        metric.setClientId(clientId);
        metricRepository.save(metric);
    }

    public MetricsStatsResponse getMetricsStats(OffsetDateTime from, OffsetDateTime to, String clientId) {
        MetricsStatsResponse stats = metricRepository.getMetricsStatsAggregation(clientId, from, to);

        if (stats == null || stats.count() == 0) {
            return new MetricsStatsResponse(0L, 0.0, 0.0, 0.0);
        }

        return stats;
    }
}

