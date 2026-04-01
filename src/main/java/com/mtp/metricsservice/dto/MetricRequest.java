package com.mtp.metricsservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.OffsetDateTime;
import java.util.Map;

public record MetricRequest(
        @NotNull(message = "Timestamp cannot be null")
        OffsetDateTime timestamp,
        @Positive(message = "Value must be positive")
        Double value,
        @NotNull(message = "Payload cannot be null")
        Map<String, Object> payload
) {}

