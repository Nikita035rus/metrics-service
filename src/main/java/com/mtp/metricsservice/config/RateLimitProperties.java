package com.mtp.metricsservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "rate-limit")
@Data
public class RateLimitProperties {
    private EndpointConfig metricsEndpoint;
    private EndpointConfig authEndpoint;

    @Data
    public static class EndpointConfig {
        private int maxRequestsPerClient;
        private int globalMaxRequests;
        private int timeWindowSeconds;
        private boolean ipBased;
    }
}
