package com.mtp.metricsservice.entity;

import com.mtp.metricsservice.util.JsonConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.Map;

@Entity
@Table(name = "metrics")
@Data
public class Metric {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "metric_timestamp")
    private OffsetDateTime timestamp;

    @Column(name = "metric_value")
    private Double value;

    @NotNull
    @Column(columnDefinition = "jsonb")
    @Convert(converter = JsonConverter.class)
    private Map<String, Object> payload;

    @Column(name = "client_id")
    private String clientId;
}

