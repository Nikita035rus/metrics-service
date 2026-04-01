package com.mtp.metricsservice.controller;

import com.mtp.metricsservice.annotation.RateLimit;
import com.mtp.metricsservice.dto.MetricRequest;
import com.mtp.metricsservice.dto.MetricsStatsResponse;
import com.mtp.metricsservice.service.MetricsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MetricsController {

    private final MetricsService metricsService;

    @PostMapping("/metrics")
    @RateLimit("metrics-endpoint")
    public ResponseEntity<Void> collectMetric(
            @Valid @RequestBody MetricRequest request,
            @AuthenticationPrincipal String clientId) {
        metricsService.saveMetric(request, clientId);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/metrics")
    @RateLimit("metrics-endpoint")
    public ResponseEntity<MetricsStatsResponse> getMetricsStats(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime to,
            @AuthenticationPrincipal String clientId) {
        MetricsStatsResponse stats = metricsService.getMetricsStats(from, to, clientId);
        return ResponseEntity.ok(stats);
    }
}

