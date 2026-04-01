package com.mtp.metricsservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AuthRequest(
        @NotBlank(message = "Client ID cannot be null or empty")
        @Pattern(regexp = "client-\\d+", message = "Client ID must start with 'client-' and contain only digits after the dash")
        String clientId) {}

