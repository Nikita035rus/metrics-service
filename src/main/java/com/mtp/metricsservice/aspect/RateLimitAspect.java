package com.mtp.metricsservice.aspect;

import com.mtp.metricsservice.annotation.RateLimit;
import com.mtp.metricsservice.config.RateLimitProperties;
import com.mtp.metricsservice.exception.RateLimitExceededException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Aspect
@Component
@RequiredArgsConstructor
public class RateLimitAspect {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RateLimitProperties properties;
    private final HttpServletRequest request;

    @Around("@annotation(rateLimit)")
    public Object limitRate(ProceedingJoinPoint joinPoint, RateLimit rateLimit) throws Throwable {
        var config = rateLimit.value().equals("auth-endpoint")
                ? properties.getAuthEndpoint()
                : properties.getMetricsEndpoint();

        String identifier = config.isIpBased()
                ? request.getRemoteAddr()
                : SecurityContextHolder.getContext().getAuthentication().getName();

        // 1. Личный лимит клиента
        if (isLimitExceeded("rate:local:" + identifier, config.getMaxRequestsPerClient(), config.getTimeWindowSeconds())) {
            throw new RateLimitExceededException("Rate limit exceeded for: " + identifier);
        }

        // 2. Глобальный лимит
        if (config.getGlobalMaxRequests() > 0) {
            if (isLimitExceeded("rate:global", config.getGlobalMaxRequests(), config.getTimeWindowSeconds())) {
                throw new RateLimitExceededException("Global rate limit exceeded");
            }
        }

        return joinPoint.proceed();
    }

    private boolean isLimitExceeded(String key, int limit, int window) {
        Long current = redisTemplate.opsForValue().increment(key);
        if (current != null && current == 1) {
            redisTemplate.expire(key, Duration.ofSeconds(window));
        }
        return current != null && current > limit;
    }
}


