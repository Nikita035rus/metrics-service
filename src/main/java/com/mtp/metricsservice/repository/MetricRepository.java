package com.mtp.metricsservice.repository;

import com.mtp.metricsservice.dto.MetricsStatsResponse;
import com.mtp.metricsservice.entity.Metric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;

@Repository
public interface MetricRepository extends JpaRepository<Metric, Long> {

    @Query("SELECT new com.mtp.metricsservice.dto.MetricsStatsResponse(" +
            "COUNT(m), " +
            "AVG(m.value), " +
            "MIN(m.value), " +
            "MAX(m.value)) " +
            "FROM Metric m " +
            "WHERE m.clientId = :clientId " +
            "AND m.timestamp BETWEEN :from AND :to")
    MetricsStatsResponse getMetricsStatsAggregation(
            @Param("clientId") String clientId,
            @Param("from") OffsetDateTime from,
            @Param("to") OffsetDateTime to
    );
}
