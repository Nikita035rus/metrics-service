package com.mtp.metricsservice.dto;

public record MetricsStatsResponse(
        Long count,
        Double avg,
        Double min,
        Double max
) {
    public MetricsStatsResponse {
        count = (count == null) ? 0L : count;
        avg = (avg == null) ? 0.0 : avg;
        min = (min == null) ? 0.0 : min;
        max = (max == null) ? 0.0 : max;
    }
}

